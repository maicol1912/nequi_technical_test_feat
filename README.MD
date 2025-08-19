# 🏢 Franquicias API – Prueba Técnica NEQUI

API de gestión de franquicias, sucursales y productos desarrollada con **arquitectura hexagonal**, soporte **reactivo** y un enfoque en **mejores prácticas** para garantizar escalabilidad, mantenibilidad y despliegue en la nube.

---

## 📐 Arquitectura

La API sigue una **arquitectura hexagonal (Ports & Adapters)**, que promueve la modularidad y la separación de responsabilidades.

```plaintext
┌─────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                     │
├─────────────────┬─────────────────┬─────────────────────────┤
│   Web Layer     │   Persistence   │     Configuration       │
│   (Controllers) │    (R2DBC)      │     (Properties)        │
├─────────────────┼─────────────────┼─────────────────────────┤
│                    APPLICATION LAYER                        │
├─────────────────────────┬───────────────────────────────────┤
│      Use Cases          │         Services                  │
│   (Input Ports)         │      (Business Logic)             │
├─────────────────────────┼───────────────────────────────────┤
│                      DOMAIN LAYER                           │
├─────────────────────────┬───────────────────────────────────┤
│       Models            │       Output Ports                │
│   (Domain Entities)     │     (Repository Interfaces)       │
└─────────────────────────┴───────────────────────────────────┘
```

---

## 🛠 Stack Tecnológico

- **Framework**: Spring Boot 3.2 + Spring WebFlux (Reactivo)
- **Lenguaje**: Java 21 (LTS)
- **Base de Datos**: PostgreSQL 15
- **ORM Reactivo**: Spring Data R2DBC
- **Migraciones**: Flyway
- **Documentación**: OpenAPI 3 + Swagger UI
- **Containerización**: Docker + Docker Compose
- **Cloud**: Google Cloud Platform (GCP)
- **IaC**: Terraform

---

## 🚀 Funcionalidades

### 1️⃣ Gestión de Franquicias
- Crear, actualizar y listar franquicias.
- Consultar franquicia por ID.
- Obtener franquicia con sucursales y productos asociados.

### 2️⃣ Gestión de Sucursales
- Agregar sucursal a una franquicia.
- Actualizar datos de una sucursal.
- Listar sucursales de una franquicia.
- Obtener sucursal con sus productos.

### 3️⃣ Gestión de Productos
- Agregar producto a una sucursal.
- Actualizar información de producto.
- Modificar stock de producto.
- Eliminar producto.
- Consultar productos por sucursal.

### 4️⃣ Reportes
- Producto con mayor stock por sucursal (optimizado con vista en BD).
- Consultas reactivas con R2DBC.

---

## 🌐 Endpoints Principales

### Franquicias
| Método | Endpoint                                    | Descripción                               |
|--------|---------------------------------------------|-------------------------------------------|
| POST   | `/api/v1/franquicias`                      | Crear franquicia                          |
| GET    | `/api/v1/franquicias`                      | Listar franquicias                        |
| GET    | `/api/v1/franquicias/{id}`                 | Obtener franquicia                        |
| PUT    | `/api/v1/franquicias/{id}`                 | Actualizar franquicia                     |
| GET    | `/api/v1/franquicias/{id}/completa`        | Franquicia con sucursales y productos     |
| GET    | `/api/v1/franquicias/{id}/productos-max-stock` | Producto con más stock                |

### Sucursales
| Método | Endpoint                                    | Descripción                               |
|--------|---------------------------------------------|-------------------------------------------|
| POST   | `/api/v1/franquicias/{id}/sucursales`      | Agregar sucursal                          |
| GET    | `/api/v1/sucursales/{id}`                  | Obtener sucursal                          |
| PUT    | `/api/v1/sucursales/{id}`                  | Actualizar sucursal                       |
| GET    | `/api/v1/sucursales/{id}/productos`        | Productos de sucursal                     |

### Productos
| Método | Endpoint                                    | Descripción                               |
|--------|---------------------------------------------|-------------------------------------------|
| POST   | `/api/v1/sucursales/{id}/productos`        | Agregar producto                          |
| GET    | `/api/v1/productos/{id}`                   | Obtener producto                          |
| PUT    | `/api/v1/productos/{id}`                   | Actualizar producto                       |
| PATCH  | `/api/v1/productos/{id}/stock`             | Modificar stock                           |
| DELETE | `/api/v1/productos/{id}`                   | Eliminar producto                         |

---

## 💻 Desarrollo Local

### 📋 Prerrequisitos
- Docker v20+
- Docker Compose v2+
- Java 21 (opcional, si se corre sin Docker)
- Maven 3.9+ (opcional, si se corre sin Docker)

### ⚡ Pasos de Instalación
1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/maicol1912/nequi_api_technical_test
   cd nequi_api_technical_test
   ```

2. **Levantar base de datos y API**:
   ```bash
   docker-compose up --build -d
   ```

3. **Verificar que el servicio esté activo**:
   ```bash
   curl http://localhost:8080/healthcheck
   ```

   **Respuesta esperada**:
   ```json
   {
     "status": "UP",
     "application": "franchise-api",
     "version": "1.0.0",
     "timestamp": "2025-08-08T14:25:31.123"
   }
   ```

---

## 📜 Documentación API
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🗄 Acceso a Base de Datos (PgAdmin)
- **URL**: [http://localhost:5050](http://localhost:5050)
- **Usuario**: `admin@example.com`
- **Contraseña**: `admin123`
- **Conexión a DB**:
    - Host: `postgres`
    - Puerto: `5432`
    - Usuario: `franchise_user`
    - Contraseña: `franchise1234**`
    - Base de datos: `franchise_nequi`

---

## ⚙️ Variables de Entorno
Definidas en `docker-compose.yml` (pueden sobreescribirse):

```bash
SPRING_PROFILES_ACTIVE=docker
DB_HOST=postgres
DB_PORT=5432
DB_NAME=franchise_nequi
DB_USERNAME=franchise_user
DB_PASSWORD=franchise1234**
SERVER_PORT=8080
```

---

## 🛠 Comandos Útiles
```bash
# Ver logs de la API
docker logs -f franchise-management-api

# Reiniciar servicios
docker-compose restart

# Apagar servicios
docker-compose down

# Apagar y eliminar volúmenes
docker-compose down -v
```

---

## 📦 Despliegue
- Listo para ejecutarse en **Google Cloud Platform (GCP)**.
- Manejo de infraestructura con **Terraform**.
- Configuración desacoplada por perfiles (`application-{profile}.yml`).

---
🧪 Testeo en Producción
La API se encuentra desplegada y disponible para pruebas en el entorno productivo.
Puedes explorar y ejecutar los endpoints directamente desde Swagger UI en el siguiente enlace:

🔗 Swagger UI – Producción
https://franchise-api-prod-api-523802894907.us-central1.run.app/webjars/swagger-ui/index.html