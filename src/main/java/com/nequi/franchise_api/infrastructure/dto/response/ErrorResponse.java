package com.nequi.franchise_api.infrastructure.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Error response information")
public record ErrorResponse(

        @Schema(description = "Error code", example = "FRANCHISE_NOT_FOUND")
        String errorCode,

        @Schema(description = "Error message", example = "Franchise not found with ID: 123e4567-e89b-12d3-a456-426614174000")
        String message,

        @Schema(description = "Detailed error information")
        String details,

        @Schema(description = "List of validation errors")
        List<ValidationError> validationErrors,

        @Schema(description = "Request path", example = "/api/v1/franchises/123e4567-e89b-12d3-a456-426614174000")
        String path,

        @Schema(description = "HTTP status code", example = "404")
        Integer status,

        @Schema(description = "Error timestamp", example = "2024-01-15T10:30:00")
        LocalDateTime timestamp
) {

    @Schema(description = "Validation error details")
    public record ValidationError(

            @Schema(description = "Field name", example = "name")
            String field,

            @Schema(description = "Rejected value", example = "")
            Object rejectedValue,

            @Schema(description = "Error message", example = "Name cannot be empty")
            String message
    ) {}
}
