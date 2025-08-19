module "franchise_infrastructure" {
  source = "./franchise-terraform"

  project_id            = var.project_id
  region                = var.region
  zone                  = var.zone
  environment           = var.environment
  db_name               = var.db_name
  db_user               = var.db_user
  db_password           = var.db_password
  db_tier               = var.db_tier
  cors_allowed_origins  = var.cors_allowed_origins
  enable_pgadmin        = var.enable_pgadmin
  enable_ci_cd          = var.enable_ci_cd
  github_repo_name      = var.github_repo_name
}
