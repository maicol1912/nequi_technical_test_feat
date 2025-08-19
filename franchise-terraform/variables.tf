variable "project_id" {
  description = "GCP Project ID"
  type        = string
}

variable "region" {
  description = "GCP Region"
  type        = string
}

variable "zone" {
  description = "GCP Zone"
  type        = string
}

variable "environment" {
  description = "Environment (dev, staging, prod)"
  type        = string
}

variable "db_name" {
  description = "Database name"
  type        = string
}

variable "db_user" {
  description = "Database username"
  type        = string
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
}

variable "db_tier" {
  description = "Database tier"
  type        = string
}

variable "cors_allowed_origins" {
  description = "CORS allowed origins"
  type        = string
}

variable "enable_pgadmin" {
  description = "Enable PgAdmin deployment"
  type        = bool
}

variable "enable_ci_cd" {
  description = "Enable CI/CD with Cloud Build"
  type        = bool
}

variable "github_repo_name" {
  description = "GitHub repository name for CI/CD"
  type        = string
}
