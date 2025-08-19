package com.nequi.franchise_api.domain.model.aggregate;

import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.ProductId;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Data
@Builder
public class ProductMaxStock {

    private final ProductId productId;
    private final String productName;
    private final BranchId branchId;
    private final String branchName;
    private final Integer stock;

    public ProductId getProductId() {
        return productId;
    }

    public BranchId getBranchId() {
        return branchId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getStock() {
        return stock;
    }

    public String getBranchName() {
        return branchName;
    }
}