#!/bin/bash

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}🚀 Desplegando infraestructura de Franchise Management${NC}"
echo "=================================================="

check_prerequisites() {
    echo -e "${YELLOW}📋 Verificando prerrequisitos...${NC}"
    if ! command -v gcloud &> /dev/null; then
        echo -e "${RED}❌ gcloud CLI no está instalado${NC}"
        exit 1
    fi
    if ! command -v terraform &> /dev/null; then
        echo -e "${RED}❌ Terraform no está instalado${NC}"
        exit 1
    fi
    if ! command -v docker &> /dev/null; then
        echo -e "${RED}❌ Docker no está instalado${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ Prerrequisitos verificados${NC}"
}

load_terraform_vars() {
    echo -e "${YELLOW}📄 Cargando variables de Terraform...${NC}"
    VARS_FILE="$(dirname "$0")/../terraform.tfvars"
    if [ -f "$VARS_FILE" ]; then
        PROJECT_ID=$(grep 'project_id' "$VARS_FILE" | cut -d '"' -f 2)
        REGION=$(grep 'region' "$VARS_FILE" | cut -d '"' -f 2)
    else
        echo -e "${RED}❌ Archivo terraform.tfvars no encontrado${NC}"
        exit 1
    fi
}

setup_gcp() {
    echo -e "${YELLOW}🔑 Configurando GCP...${NC}"
    gcloud auth login
    gcloud config set project $PROJECT_ID
    gcloud config set compute/region $REGION
    gcloud auth application-default login
    echo -e "${GREEN}✅ GCP configurado${NC}"
}

terraform_apply() {
    echo -e "${YELLOW}🏗️  Aplicando configuración de Terraform...${NC}"
    terraform init
    terraform validate
    terraform plan -out=tfplan
    terraform apply -auto-approve tfplan
    echo -e "${GREEN}✅ Infraestructura desplegada${NC}"
}

build_and_push_docker() {
    echo -e "${YELLOW}🐳 Construyendo y subiendo imagen Docker...${NC}"
    cd ../..
    ARTIFACT_REGISTRY=$(terraform output -raw artifact_registry_url)
    docker build -t franchise-api:latest .
    docker tag franchise-api:latest ${ARTIFACT_REGISTRY}/franchise-api:latest
    gcloud auth configure-docker ${REGION}-docker.pkg.dev
    docker push ${ARTIFACT_REGISTRY}/franchise-api:latest
    cd franchise-terraform
    echo -e "${GREEN}✅ Imagen Docker subida${NC}"
}

main() {
    check_prerequisites
    load_terraform_vars
    setup_gcp
    terraform_apply
    build_and_push_docker
    echo -e "${GREEN}🎉 ¡Despliegue completado exitosamente!${NC}"
    echo -e "${BLUE}URL de la API:${NC} $(terraform output -raw cloud_run_url)"
}

main "$@"