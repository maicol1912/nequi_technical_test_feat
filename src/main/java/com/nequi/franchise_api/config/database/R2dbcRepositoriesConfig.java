package com.nequi.franchise_api.config.database;

import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository")
public class R2dbcRepositoriesConfig {

}
