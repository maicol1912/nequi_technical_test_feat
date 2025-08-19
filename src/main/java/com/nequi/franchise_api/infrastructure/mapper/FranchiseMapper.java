package com.nequi.franchise_api.infrastructure.mapper;

import com.nequi.franchise_api.domain.model.aggregate.FranchiseAggregate;
import com.nequi.franchise_api.domain.model.entity.Franchise;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.domain.model.valueobject.Name;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import com.nequi.franchise_api.infrastructure.dto.response.FranchiseDetailResponse;
import com.nequi.franchise_api.infrastructure.dto.response.FranchiseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {BranchMapper.class})
public interface FranchiseMapper {

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.value", target = "name")
    FranchiseResponse toResponse(Franchise franchise);

    @Mapping(source = "franchise.id.value", target = "id")
    @Mapping(source = "franchise.name.value", target = "name")
    @Mapping(source = "franchise.createdAt", target = "createdAt")
    @Mapping(source = "franchise.updatedAt", target = "updatedAt")
    @Mapping(source = "branches", target = "branches")
    @Mapping(source = ".", target = "totalBranches", qualifiedByName = "getTotalBranches")
    FranchiseDetailResponse toDetailResponse(FranchiseAggregate franchiseAggregate);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "name", target = "name.value")
    Franchise toDomain(FranchiseEntity entity);

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.value", target = "name")
    FranchiseEntity toEntity(Franchise franchise);

    @Named("getTotalBranches")
    default Integer getTotalBranches(FranchiseAggregate aggregate) {
        return aggregate.getTotalBranches();
    }

    default FranchiseId map(UUID value) {
        return value != null ? FranchiseId.of(value) : null;
    }

    default UUID map(FranchiseId franchiseId) {
        return franchiseId != null ? franchiseId.getValue() : null;
    }
}
