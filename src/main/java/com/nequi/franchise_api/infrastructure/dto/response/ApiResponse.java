package com.nequi.franchise_api.infrastructure.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Generic API response wrapper")
public record ApiResponse<T>(

        @Schema(description = "Response status", example = "SUCCESS")
        String status,

        @Schema(description = "Response message", example = "Operation completed successfully")
        String message,

        @Schema(description = "Response data")
        T data,

        @Schema(description = "Response timestamp", example = "2024-01-15T10:30:00")
        LocalDateTime timestamp,

        @Schema(description = "Total count for list responses", example = "25")
        Integer count
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", "Operation completed successfully", data, LocalDateTime.now(),null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("SUCCESS", message, data, LocalDateTime.now(),null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null, LocalDateTime.now(),null);
    }

    public static <T> ApiResponse<List<T>> successList(List<T> data) {
        return new ApiResponse<>("SUCCESS",
                String.format("Retrieved %d items successfully", data.size()),
                data,
                LocalDateTime.now(),
                data.size());
    }

    public static <T> ApiResponse<List<T>> successList(String message, List<T> data) {
        return new ApiResponse<>("SUCCESS",
                message,
                data,
                LocalDateTime.now(),
                data.size());
    }

    public static <T> ApiResponse<List<T>> emptyList(String message) {
        return new ApiResponse<>("SUCCESS",
                message,
                List.of(),
                LocalDateTime.now(),
                0);
    }

    public boolean isEmpty() {
        if (data instanceof List<?> list) {
            return list.isEmpty();
        }
        return data == null;
    }

    public boolean isSuccess() {
        return "SUCCESS".equals(status);
    }
}
