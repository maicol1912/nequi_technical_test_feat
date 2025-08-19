package com.nequi.franchise_api.infrastructure.mapper;

import com.nequi.franchise_api.domain.model.aggregate.BranchWithProducts;
import com.nequi.franchise_api.domain.model.aggregate.FranchiseAggregate;
import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.entity.Franchise;
import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.model.valueobject.*;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.projection.FranchiseRawData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FranchiseAggregateFactory {

    public static FranchiseAggregate fromRawData(List<FranchiseRawData> rawDataList) {
        if (rawDataList == null || rawDataList.isEmpty()) {
            throw new IllegalArgumentException("El rawDataList no puede ser vacío");
        }

        FranchiseRawData first = rawDataList.get(0);

        Franchise franchise = new Franchise(
                new FranchiseId(UUID.fromString(first.getFranchiseId())),
                new Name(first.getFranchiseName()),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Map<String, List<FranchiseRawData>> byBranch = rawDataList.stream()
                .collect(Collectors.groupingBy(FranchiseRawData::getBranchId));

        List<BranchWithProducts> branches = byBranch.entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .map(entry -> {
                    String branchId = entry.getKey();
                    List<FranchiseRawData> branchData = entry.getValue();

                    FranchiseRawData bd = branchData.get(0);

                    Branch branch = new Branch(
                            new BranchId(UUID.fromString(bd.getBranchId())),
                            franchise.getId(),
                            new Name(bd.getBranchName()),
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    );

                    List<Product> products = branchData.stream()
                            .filter(r -> r.getProductId() != null)
                            .map(r -> new Product(
                                    new ProductId(UUID.fromString(r.getProductId())),
                                    new BranchId(UUID.fromString(bd.getBranchId())),
                                    new Name(r.getProductName()),
                                    new Stock(r.getProductStock()),
                                    LocalDateTime.now(),
                                    LocalDateTime.now()
                            ))
                            .collect(Collectors.toList());

                    return new BranchWithProducts(branch, products);
                })
                .collect(Collectors.toList());

        return new FranchiseAggregate(franchise, branches);
    }
}

