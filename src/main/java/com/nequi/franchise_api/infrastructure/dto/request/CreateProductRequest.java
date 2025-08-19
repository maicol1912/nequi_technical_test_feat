package com.nequi.franchise_api.infrastructure.dto.request;

import com.nequi.franchise_api.infrastructure.validation.annotation.ValidName;
import com.nequi.franchise_api.infrastructure.validation.annotation.ValidStock;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new product")
public record CreateProductRequest(

        @Schema(description = "Product name", example = "Big Mac", required = true)
        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
        @ValidName
        String name,

        @Schema(description = "Initial stock quantity", example = "50", required = true)
        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock cannot be negative")
        @ValidStock
        Integer stock
) {}
