package com.nequi.franchise_api.infrastructure.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Branch information with its products")
public record BranchWithProductsResponse(

        @Schema(description = "Branch unique identifier", example = "123e4567-e89b-12d3-a456-426614174001")
        String id,

        @Schema(description = "Branch name", example = "Downtown Branch")
        String name,

        @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp", example = "2024-01-15T14:30:00")
        LocalDateTime updatedAt,

        @Schema(description = "List of products in this branch")
        List<ProductResponse> products,

        @Schema(description = "Total number of products", example = "5")
        Integer totalProducts,

        @Schema(description = "Total stock across all products", example = "250")
        Integer totalStock
) {}
