#!/bin/bash

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}🔑 Configurando Workload Identity Federation para GitHub Actions${NC}"

load_terraform_vars() {
    VARS_FILE="$(dirname "$0")/../terraform.tfvars"
    if [ -f "$VARS_FILE" ]; then
        PROJECT_ID=$(grep 'project_id' "$VARS_FILE" | cut -d '"' -f 2)
        REGION=$(grep 'region' "$VARS_FILE" | cut -d '"' -f 2)
        GITHUB_REPO=$(grep 'github_repo_name' "$VARS_FILE" | cut -d '"' -f 2)
        if [ -z "$PROJECT_ID" ] || [ -z "$REGION" ] || [ -z "$GITHUB_REPO" ]; then
            echo -e "${RED}❌ Variables incompletas en terraform.tfvars${NC}"
            exit 1
        fi
        # Extract repository owner from the GitHub repo name
        REPO_OWNER=$(echo "$GITHUB_REPO" | cut -d '/' -f 1)
        echo -e "${GREEN}✅ Variables cargadas: PROJECT_ID=$PROJECT_ID, REGION=$REGION, GITHUB_REPO=$GITHUB_REPO${NC}"
        echo -e "${GREEN}✅ Repository owner: $REPO_OWNER${NC}"
    else
        echo -e "${RED}❌ Archivo terraform.tfvars no encontrado${NC}"
        exit 1
    fi
}

run_with_timeout() {
    local cmd="$1"
    local timeout="${2:-60}"
    local description="$3"

    echo -e "${YELLOW}⏳ $description (timeout: ${timeout}s)...${NC}"

    if timeout "$timeout" bash -c "$cmd"; then
        echo -e "${GREEN}✅ $description completado${NC}"
        return 0
    else
        echo -e "${RED}❌ $description falló o timeout${NC}"
        return 1
    fi
}

cleanup_existing_resources() {
    echo -e "${YELLOW}🧹 Limpiando recursos existentes...${NC}"

    if gcloud iam workload-identity-pools providers describe $PROVIDER_NAME --workload-identity-pool=$POOL_NAME --location="global" --project=$PROJECT_ID >/dev/null 2>&1; then
        echo -e "${YELLOW}🗑️  Eliminando Workload Identity Provider existente...${NC}"
        gcloud iam workload-identity-pools providers delete $PROVIDER_NAME \
            --workload-identity-pool=$POOL_NAME \
            --location="global" \
            --project=$PROJECT_ID \
            --quiet || echo -e "${YELLOW}⚠️  No se pudo eliminar el provider${NC}"
        echo -e "${YELLOW}⏳ Esperando eliminación completa...${NC}"
        sleep 30
    fi
}

POOL_NAME="github-actions-pool"
PROVIDER_NAME="github-actions-provider"
SA_NAME="github-actions-sa"

load_terraform_vars

echo -e "${BLUE}🔧 Configuración WIF para proyecto: $PROJECT_ID${NC}"
echo -e "${BLUE}🔧 Repository owner: $REPO_OWNER${NC}"

read -p "¿Continuar con la configuración? (y/N): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}⚠️  Configuración cancelada${NC}"
    exit 0
fi

echo -e "${YELLOW}🔧 Configurando proyecto GCP...${NC}"
gcloud config set project $PROJECT_ID
gcloud config set compute/region $REGION

echo -e "${YELLOW}🔧 Habilitando APIs necesarias...${NC}"
gcloud services enable iam.googleapis.com \
    cloudresourcemanager.googleapis.com \
    iamcredentials.googleapis.com \
    sts.googleapis.com \
    --project=$PROJECT_ID

echo -e "${YELLOW}👤 Creando/verificando Service Account...${NC}"
if ! gcloud iam service-accounts describe $SA_NAME@$PROJECT_ID.iam.gserviceaccount.com --project=$PROJECT_ID &>/dev/null; then
    gcloud iam service-accounts create $SA_NAME \
        --display-name="GitHub Actions Service Account" \
        --description="Service Account para GitHub Actions CI/CD" \
        --project=$PROJECT_ID
    echo -e "${GREEN}✅ Service Account creado${NC}"
else
    echo -e "${GREEN}✅ Service Account ya existe${NC}"
fi

echo -e "${YELLOW}👥 Asignando roles al Service Account...${NC}"
ROLES=(
    "roles/compute.admin"
    "roles/iam.serviceAccountUser"
    "roles/cloudsql.admin"
    "roles/run.admin"
    "roles/artifactregistry.admin"
    "roles/storage.admin"
    "roles/vpcaccess.admin"
    "roles/serviceusage.serviceUsageAdmin"
    "roles/cloudbuild.builds.editor"
    "roles/secretmanager.secretAccessor"
)
for role in "${ROLES[@]}"; do
    echo "  → Asignando rol: $role"
    gcloud projects add-iam-policy-binding $PROJECT_ID \
        --member="serviceAccount:$SA_NAME@$PROJECT_ID.iam.gserviceaccount.com" \
        --role="$role" \
        --quiet >/dev/null 2>&1 || echo -e "${YELLOW}  ⚠️  Rol $role ya asignado o error${NC}"
done

echo -e "${YELLOW}📦 Creando/verificando Workload Identity Pool...${NC}"
if ! gcloud iam workload-identity-pools describe $POOL_NAME --location="global" --project=$PROJECT_ID &>/dev/null; then
    run_with_timeout "gcloud iam workload-identity-pools create $POOL_NAME --location='global' --display-name='GitHub Actions Pool' --description='Pool para GitHub Actions' --project=$PROJECT_ID" 120 "Creación de Workload Identity Pool" || {
        echo -e "${RED}❌ Error creando Workload Identity Pool${NC}"
        exit 1
    }
else
    echo -e "${GREEN}✅ Workload Identity Pool ya existe${NC}"
fi

PROJECT_NUMBER=$(gcloud projects describe $PROJECT_ID --format="value(projectNumber)")
WORKLOAD_IDENTITY_POOL_ID="projects/$PROJECT_NUMBER/locations/global/workloadIdentityPools/$POOL_NAME"

echo -e "${YELLOW}🔗 Manejando Workload Identity Provider...${NC}"

PROVIDER_EXISTS=false
PROVIDER_CONDITION_OK=false

if gcloud iam workload-identity-pools providers describe $PROVIDER_NAME --workload-identity-pool=$POOL_NAME --location="global" --project=$PROJECT_ID >/dev/null 2>&1; then
    PROVIDER_EXISTS=true
    echo -e "${GREEN}✅ Workload Identity Provider existe${NC}"

    CURRENT_CONDITION=$(gcloud iam workload-identity-pools providers describe $PROVIDER_NAME --workload-identity-pool=$POOL_NAME --location="global" --project=$PROJECT_ID --format="value(attributeCondition)" || echo "")
    EXPECTED_CONDITION="assertion.repository_owner=='$REPO_OWNER'"

    if [[ "$CURRENT_CONDITION" == "$EXPECTED_CONDITION" ]]; then
        PROVIDER_CONDITION_OK=true
        echo -e "${GREEN}✅ Configuración del provider es correcta${NC}"
    else
        echo -e "${YELLOW}⚠️  Configuración del provider incorrecta${NC}"
        echo -e "${YELLOW}    Actual: '$CURRENT_CONDITION'${NC}"
        echo -e "${YELLOW}    Esperada: '$EXPECTED_CONDITION'${NC}"
    fi
fi

if [[ "$PROVIDER_EXISTS" == "false" ]] || [[ "$PROVIDER_CONDITION_OK" == "false" ]]; then
    [[ "$PROVIDER_EXISTS" == "true" ]] && cleanup_existing_resources

    echo -e "${YELLOW}🔧 Creando nuevo Workload Identity Provider...${NC}"

    CREATE_CMD="gcloud iam workload-identity-pools providers create-oidc $PROVIDER_NAME \
        --workload-identity-pool=$POOL_NAME \
        --location='global' \
        --issuer-uri='https://token.actions.githubusercontent.com' \
        --attribute-mapping='google.subject=assertion.sub,attribute.actor=assertion.actor,attribute.repository=assertion.repository,attribute.repository_owner=assertion.repository_owner' \
        --attribute-condition=\"assertion.repository_owner=='$REPO_OWNER'\" \
        --project=$PROJECT_ID"

    if run_with_timeout "$CREATE_CMD" 180 "Creación de Workload Identity Provider"; then
        echo -e "${GREEN}✅ Workload Identity Provider creado exitosamente${NC}"
    else
        echo -e "${RED}❌ Error creando Workload Identity Provider${NC}"
        echo -e "${YELLOW}💡 Intentando creación simplificada...${NC}"
        gcloud iam workload-identity-pools providers create-oidc $PROVIDER_NAME \
            --workload-identity-pool=$POOL_NAME \
            --location="global" \
            --issuer-uri="https://token.actions.githubusercontent.com" \
            --attribute-mapping="google.subject=assertion.sub,attribute.repository=assertion.repository" \
            --project=$PROJECT_ID || {
            echo -e "${RED}❌ Creación simplificada también falló${NC}"
            exit 1
        }
    fi
fi

echo -e "${YELLOW}👥 Configurando impersonation...${NC}"

configure_impersonation() {
    gcloud iam service-accounts add-iam-policy-binding \
        "$SA_NAME@$PROJECT_ID.iam.gserviceaccount.com" \
        --role="roles/iam.workloadIdentityUser" \
        --member="principalSet://iam.googleapis.com/$WORKLOAD_IDENTITY_POOL_ID/attribute.repository/$GITHUB_REPO" \
        --project="$PROJECT_ID"
}

if configure_impersonation; then
    echo -e "${GREEN}✅ Impersonation configurado correctamente${NC}"
else
    echo -e "${RED}❌ Error configurando impersonation${NC}"
    echo -e "${YELLOW}💡 Verifica el nombre del repositorio: $GITHUB_REPO${NC}"
    exit 1
fi

echo -e "${YELLOW}🔍 Verificando configuración final...${NC}"

echo -e "${BLUE}📋 Resumen de configuración:${NC}"
echo -e "  • Proyecto: $PROJECT_ID"
echo -e "  • Región: $REGION"
echo -e "  • Repositorio GitHub: $GITHUB_REPO"
echo -e "  • Repository Owner: $REPO_OWNER"
echo -e "  • Service Account: $SA_NAME@$PROJECT_ID.iam.gserviceaccount.com"
echo -e "  • Workload Identity Pool: $WORKLOAD_IDENTITY_POOL_ID"

WIF_PROVIDER=$(gcloud iam workload-identity-pools providers describe $PROVIDER_NAME --workload-identity-pool=$POOL_NAME --location="global" --project=$PROJECT_ID --format='value(name)' 2>/dev/null)
SA_EMAIL="$SA_NAME@$PROJECT_ID.iam.gserviceaccount.com"

echo -e "${GREEN}🎯 Configuración para GitHub Secrets:${NC}"
echo -e "  • GCP_PROJECT_ID: $PROJECT_ID"
echo -e "  • GCP_REGION: $REGION"
echo -e "  • GCP_SERVICE_ACCOUNT: $SA_EMAIL"
echo -e "  • GCP_WORKLOAD_IDENTITY_PROVIDER: $WIF_PROVIDER"

chmod +x "$(dirname "$0")/add-github-secrets.sh"

echo -e "${GREEN}🎉 Configuración de WIF completada con éxito${NC}"
echo -e "${BLUE}➡️  Próximo paso: Ejecutar add-github-secrets.sh para configurar los secrets en GitHub${NC}"