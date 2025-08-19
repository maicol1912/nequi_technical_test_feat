package com.nequi.franchise_api.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.Name;
import com.nequi.franchise_api.domain.model.valueobject.ProductId;
import com.nequi.franchise_api.domain.model.valueobject.Stock;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@AllArgsConstructor()
@Builder
public class Product {

    private final ProductId id;
    private final BranchId branchId;
    private Name name;
    private Stock stock;
    @JsonIgnore
    private final LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;

    public static Product create(BranchId branchId, Name name, Stock stock) {
        LocalDateTime now = LocalDateTime.now();
        return new Product(
                null,
                branchId,
                name,
                stock,
                now,
                now
        );
    }

    public static Product restore(ProductId id, BranchId branchId, Name name, Stock stock,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Product(id, branchId, name, stock, createdAt, updatedAt);
    }

    public void updateName(Name newName) {
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStock(Stock newStock) {
        this.stock = newStock;
        this.updatedAt = LocalDateTime.now();
    }

    public void addStock(int quantity) {
        this.stock = this.stock.add(quantity);
        this.updatedAt = LocalDateTime.now();
    }

    public void reduceStock(int quantity) {
        this.stock = this.stock.subtract(quantity);
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasMoreStockThan(Product other) {
        return this.stock.isGreaterThan(other.stock);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}