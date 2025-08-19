package com.nequi.franchise_api.config.database;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Slf4j
@Configuration
public class DatabaseConfig extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.url}")
    private String r2dbcUrl;

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.host}")
    private String host;

    @Value("${spring.r2dbc.database}")
    private String database;

    @Value("${spring.r2dbc.password}")
    private String password;

    @Override
    @Bean
    @Primary
    public ConnectionFactory connectionFactory() {
        // Example for PostgreSQL
        PostgresqlConnectionConfiguration config = PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(5432)
                .database(database)
                .username(username)
                .password(password)
                .build();

        return new PostgresqlConnectionFactory(config);
    }

    @Bean
    @ConditionalOnProperty(name = "app.database.initialize", havingValue = "true")
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        log.info("Initializing database with sample data");

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("db/sample-data.sql"));

        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}