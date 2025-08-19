package com.nequi.franchise_api.infrastructure.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Product information response")
public record ProductResponse(

        @Schema(description = "Product unique identifier", example = "123e4567-e89b-12d3-a456-426614174002")
        String id,

        @Schema(description = "Branch ID this product belongs to", example = "123e4567-e89b-12d3-a456-426614174001")
        String branchId,

        @Schema(description = "Product name", example = "Big Mac")
        String name,

        @Schema(description = "Current stock quantity", example = "50")
        Integer stock,

        @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp", example = "2024-01-15T14:30:00")
        LocalDateTime updatedAt
) {}
