variable "project_id" {
  description = "GCP Project ID"
  type        = string
}

variable "region" {
  description = "GCP Region"
  type        = string
  default     = "us-central1"
}

variable "zone" {
  description = "GCP Zone"
  type        = string
  default     = "us-central1-a"
}

variable "environment" {
  description = "Environment (dev, staging, prod)"
  type        = string
  default     = "prod"
}

variable "db_name" {
  description = "Database name"
  type        = string
  default     = "franchise_nequi"
}

variable "db_user" {
  description = "Database username"
  type        = string
  default     = "franchise_user"
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
}

variable "db_tier" {
  description = "Database tier"
  type        = string
  default     = "db-f1-micro"
}

variable "cors_allowed_origins" {
  description = "CORS allowed origins"
  type        = string
  default     = "http://localhost:3000,http://localhost:4200"
}

variable "enable_pgadmin" {
  description = "Enable PgAdmin deployment"
  type        = bool
  default     = false
}

variable "enable_ci_cd" {
  description = "Enable CI/CD with Cloud Build"
  type        = bool
  default     = true
}

variable "github_repo_name" {
  description = "GitHub repository name for CI/CD"
  type        = string
}
