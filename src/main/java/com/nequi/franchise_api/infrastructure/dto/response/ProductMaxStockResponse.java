package com.nequi.franchise_api.infrastructure.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product with maximum stock information by branch")
public record ProductMaxStockResponse(

        @Schema(description = "Product unique identifier", example = "123e4567-e89b-12d3-a456-426614174002")
        String productId,

        @Schema(description = "Product name", example = "Big Mac")
        String productName,

        @Schema(description = "Branch unique identifier", example = "123e4567-e89b-12d3-a456-426614174001")
        String branchId,

        @Schema(description = "Branch name", example = "Downtown Branch")
        String branchName,

        @Schema(description = "Maximum stock quantity", example = "150")
        Integer stock
) {}
