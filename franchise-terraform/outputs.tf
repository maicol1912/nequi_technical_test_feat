output "cloud_run_url" {
  description = "URL of the deployed application"
  value       = google_cloud_run_v2_service.franchise_api.uri
}

output "database_connection_name" {
  description = "Database connection name"
  value       = google_sql_database_instance.postgres.connection_name
}

output "database_private_ip" {
  description = "Database private IP"
  value       = google_sql_database_instance.postgres.private_ip_address
  sensitive   = true
}

output "artifact_registry_url" {
  description = "Artifact Registry URL"
  value       = "${var.region}-docker.pkg.dev/${var.project_id}/${google_artifact_registry_repository.repo.repository_id}"
}

output "vpc_network_name" {
  description = "VPC network name"
  value       = google_compute_network.vpc_network.name
}

output "service_account_email" {
  description = "Service account email"
  value       = google_service_account.franchise_sa.email
}

output "deployment_summary" {
  description = "Summary of deployed resources"
  value = {
    api_url           = google_cloud_run_v2_service.franchise_api.uri
    database_name     = var.db_name
    registry_url      = "${var.region}-docker.pkg.dev/${var.project_id}/${google_artifact_registry_repository.repo.repository_id}"
  }
}