package com.nequi.franchise_api.infrastructure.dto.request;

import com.nequi.franchise_api.infrastructure.validation.annotation.ValidStock;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to update product stock")
public record UpdateStockRequest(

        @Schema(description = "New stock quantity", example = "75", required = true)
        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock cannot be negative")
        @ValidStock
        Integer stock
) {}
