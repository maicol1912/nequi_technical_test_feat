package com.nequi.franchise_api.domain.model.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class BranchWithProducts {
    private final Branch branch;
    private final List<Product> products;

    @JsonIgnore
    public BranchId getId() {
        return branch.getId();
    }
    @JsonIgnore
    public FranchiseId getFranchiseId() {
        return branch.getFranchiseId();
    }
    @JsonIgnore
    public String getName() {
        return branch.getName().getValue();
    }

    public int getTotalProducts() {
        return products.size();
    }

    public int getTotalStock() {
        return products.stream()
                .mapToInt(product -> product.getStock().getValue())
                .sum();
    }
    @JsonIgnore
    public Optional<Product> getProductWithMaxStock() {
        return products.stream()
                .max((p1, p2) -> Integer.compare(
                        p1.getStock().getValue(),
                        p2.getStock().getValue()
                ));
    }
    @JsonIgnore
    public boolean hasProducts() {
        return !products.isEmpty();
    }
}