package com.nequi.franchise_api.infrastructure.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Franchise information response")
public record FranchiseResponse(

        @Schema(description = "Franchise unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Franchise name", example = "McDonald's")
        String name,

        @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp", example = "2024-01-15T14:30:00")
        LocalDateTime updatedAt
) {}
