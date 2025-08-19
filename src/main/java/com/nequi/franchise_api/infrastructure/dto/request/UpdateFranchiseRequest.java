package com.nequi.franchise_api.infrastructure.dto.request;

import com.nequi.franchise_api.infrastructure.validation.annotation.ValidName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to update franchise information")
public record UpdateFranchiseRequest(

        @Schema(description = "New franchise name", example = "McDonald's Updated", required = true)
        @NotBlank(message = "Franchise name is required")
        @Size(min = 2, max = 100, message = "Franchise name must be between 2 and 100 characters")
        @ValidName
        String name
) {}
