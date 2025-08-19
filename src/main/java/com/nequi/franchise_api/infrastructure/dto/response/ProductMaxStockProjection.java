package com.nequi.franchise_api.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMaxStockProjection {
    private UUID id;
    private UUID branch_id;
    private String name;
    private Integer stock;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String branch_name;

    public UUID getBranchId() { return branch_id; }
    public LocalDateTime getCreatedAt() { return created_at; }
    public LocalDateTime getUpdatedAt() { return updated_at; }
    public String getBranchName() { return branch_name; }
}