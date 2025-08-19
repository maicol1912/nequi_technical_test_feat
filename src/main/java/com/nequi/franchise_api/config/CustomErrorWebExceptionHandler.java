package com.nequi.franchise_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2)
public class CustomErrorWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        // Configurar respuesta como JSON
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.getHeaders().add("Content-Type", "application/json");

        // Crear respuesta simple con mensaje y path
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage() != null ? ex.getMessage() : "An error occurred");
        errorResponse.put("path", exchange.getRequest().getPath().value());

        // Convertir a JSON y enviar
        String jsonResponse = convertToJson(errorResponse);
        DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes());

        return response.writeWith(Mono.just(buffer));
    }

    private String convertToJson(Map<String, Object> errorResponse) {
        try {
            return objectMapper.writeValueAsString(errorResponse);
        } catch (Exception e) {
            return "{\"message\":\"An error occurred\"}";
        }

    }
    }