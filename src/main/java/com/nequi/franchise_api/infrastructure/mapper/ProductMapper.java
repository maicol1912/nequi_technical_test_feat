package com.nequi.franchise_api.infrastructure.mapper;

import com.nequi.franchise_api.domain.model.aggregate.ProductMaxStock;
import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.Name;
import com.nequi.franchise_api.domain.model.valueobject.ProductId;
import com.nequi.franchise_api.domain.model.valueobject.Stock;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.nequi.franchise_api.infrastructure.dto.response.ProductMaxStockResponse;
import com.nequi.franchise_api.infrastructure.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "branchId.value", target = "branchId")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "stock.value", target = "stock")
    ProductResponse toResponse(Product product);

    @Mapping(source = "productId.value", target = "productId")
    @Mapping(source = "productName", target = "productName")
    @Mapping(source = "branchId.value", target = "branchId")
    @Mapping(source = "branchName", target = "branchName")
    @Mapping(source = "stock", target = "stock")
    ProductMaxStockResponse toMaxStockResponse(ProductMaxStock productMaxStock);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "branchId", target = "branchId.value")
    @Mapping(source = "name", target = "name.value")
    @Mapping(source = "stock", target = "stock.value")
    Product toDomain(ProductEntity entity);

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "branchId.value", target = "branchId")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "stock.value", target = "stock")
    ProductEntity toEntity(Product product);

    default ProductId mapProductId(UUID value) {
        return value != null ? ProductId.of(value) : null;
    }

    default UUID mapProductId(ProductId productId) {
        return productId != null ? productId.getValue() : null;
    }

    default BranchId mapBranchId(UUID value) {
        return value != null ? BranchId.of(value) : null;
    }

    default UUID mapBranchId(BranchId branchId) {
        return branchId != null ? branchId.getValue() : null;
    }

    default Name mapName(String value) {
        return value != null ? Name.of(value) : null;
    }

    default String mapName(Name name) {
        return name != null ? name.getValue() : null;
    }

    default Stock mapStock(Integer value) {
        return value != null ? Stock.of(value) : null;
    }

    default Integer mapStock(Stock stock) {
        return stock != null ? stock.getValue() : null;
    }
}
