package com.nequi.franchise_api.infrastructure.mapper;

import com.nequi.franchise_api.domain.model.aggregate.BranchWithProducts;
import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.domain.model.valueobject.Name;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.BranchEntity;
import com.nequi.franchise_api.infrastructure.dto.response.BranchResponse;
import com.nequi.franchise_api.infrastructure.dto.response.BranchWithProductsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface BranchMapper {

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "franchiseId.value", target = "franchiseId")
    @Mapping(source = "name.value", target = "name")
    BranchResponse toResponse(Branch branch);

    @Mapping(source = "branch.id.value", target = "id")
    @Mapping(source = "branch.name.value", target = "name")
    @Mapping(source = "branch.createdAt", target = "createdAt")
    @Mapping(source = "branch.updatedAt", target = "updatedAt")
    @Mapping(source = "products", target = "products")
    @Mapping(source = ".", target = "totalProducts", qualifiedByName = "getTotalProducts")
    @Mapping(source = ".", target = "totalStock", qualifiedByName = "getTotalStock")
    BranchWithProductsResponse toWithProductsResponse(BranchWithProducts branchWithProducts);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "franchiseId", target = "franchiseId.value")
    @Mapping(source = "name", target = "name.value")
    Branch toDomain(BranchEntity entity);

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "franchiseId.value", target = "franchiseId")
    @Mapping(source = "name.value", target = "name")
    BranchEntity toEntity(Branch branch);

    @Named("getTotalProducts")
    default Integer getTotalProducts(BranchWithProducts branchWithProducts) {
        return branchWithProducts.getTotalProducts();
    }

    @Named("getTotalStock")
    default Integer getTotalStock(BranchWithProducts branchWithProducts) {
        return branchWithProducts.getTotalStock();
    }

    default BranchId mapBranchId(UUID value) {
        return value != null ? BranchId.of(value) : null;
    }

    default UUID mapBranchId(BranchId branchId) {
        return branchId != null ? branchId.getValue() : null;
    }

    default FranchiseId mapFranchiseId(UUID value) {
        return value != null ? FranchiseId.of(value) : null;
    }

    default UUID mapFranchiseId(FranchiseId franchiseId) {
        return franchiseId != null ? franchiseId.getValue() : null;
    }

    default Name mapName(String value) {
        return value != null ? Name.of(value) : null;
    }

    default String mapName(Name name) {
        return name != null ? name.getValue() : null;
    }
}
