package com.nequi.franchise_api.config.documentation;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${app.name:Franchise Management API}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.description:Reactive API for managing franchises, branches, and products}")
    private String appDescription;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(appName)
                        .description(appDescription)
                        .version(appVersion)
                        .contact(new Contact()
                                .name("Maicol Arcila")
                                .email("maicoldev05@gmail.com")))
                .servers(List.of(
                        new Server()
                                .url("https://franchise-api-prod-api-523802894907.us-central1.run.app")
                                .description("api production server")))
                .externalDocs(new ExternalDocumentation()
                        .description("Franchise API Documentation")
                        .url("https://docs.nequi.com/franchise-api"));
    }

    @Bean
    public GroupedOpenApi franchiseApi() {
        return GroupedOpenApi.builder()
                .group("franchise-api")
                .displayName("Franchise Management API")
                .pathsToMatch("/api/v1/**")
                .packagesToScan("com.nequi.franchise_api")
                .build();
    }
}