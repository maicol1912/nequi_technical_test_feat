package com.nequi.franchise_api.infrastructure.dto.request;

import com.nequi.franchise_api.infrastructure.validation.annotation.ValidName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new branch")
public record CreateBranchRequest(

        @Schema(description = "Branch name", example = "Downtown Branch", required = true)
        @NotBlank(message = "Branch name is required")
        @Size(min = 2, max = 100, message = "Branch name must be between 2 and 100 characters")
        @ValidName
        String name
) {}
