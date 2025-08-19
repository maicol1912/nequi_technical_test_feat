package com.nequi.franchise_api.config;

import com.nequi.franchise_api.shared.utils.UuidGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.util.UUID;

@Configuration
public class ApplicationConfig {

    @Bean
    @Primary
    public Clock systemClock() {
        return Clock.systemUTC();
    }

    @Bean
    public UuidGenerator uuidGenerator() {
        return UUID::randomUUID;
    }
}
