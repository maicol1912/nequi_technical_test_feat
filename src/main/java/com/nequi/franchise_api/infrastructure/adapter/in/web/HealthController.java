package com.nequi.franchise_api.infrastructure.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/healthcheck")
@Tag(name = "Health", description = "Application health check operations")
public class HealthController {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @GetMapping
    @Operation(summary = "Health check", description = "Returns application health status")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Application is healthy")
    })
    public Mono<Map<String, Object>> healthCheck() {
        log.debug("Health check requested");

        return Mono.just(Map.of(
                "status", "UP",
                "application", appName,
                "version", appVersion,
                "timestamp", LocalDateTime.now()
        ));
    }
}