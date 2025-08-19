package com.nequi.franchise_api.infrastructure.dto.request;

import com.nequi.franchise_api.infrastructure.validation.annotation.ValidName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new franchise")
public record CreateFranchiseRequest(

        @Schema(description = "Franchise name", example = "McDonald's", required = true)
        @NotBlank(message = "Franchise name is required")
        @Size(min = 2, max = 100, message = "Franchise name must be between 2 and 100 characters")
        @ValidName
        String name
) {}
