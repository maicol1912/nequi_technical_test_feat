package com.nequi.franchise_api.infrastructure.adapter.out.persistence.adapter;

import com.nequi.franchise_api.domain.model.aggregate.FranchiseAggregate;
import com.nequi.franchise_api.domain.model.aggregate.ProductMaxStock;
import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.model.valueobject.*;
import com.nequi.franchise_api.domain.port.out.FranchiseQueryRepository;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.BranchEntity;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.projection.FranchiseRawData;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository.FranchiseQueryR2dbcRepository;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository.ProductQueryRepository;
import com.nequi.franchise_api.infrastructure.dto.response.ProductMaxStockProjection;
import com.nequi.franchise_api.infrastructure.dto.response.ProductMaxStockResponse;
import com.nequi.franchise_api.infrastructure.mapper.BranchMapper;
import com.nequi.franchise_api.infrastructure.mapper.FranchiseAggregateFactory;
import com.nequi.franchise_api.infrastructure.mapper.FranchiseMapper;
import com.nequi.franchise_api.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FranchiseQueryRepositoryAdapter implements FranchiseQueryRepository {

    private final FranchiseQueryR2dbcRepository franchiseQueryR2dbcRepository;
    private final ProductQueryRepository productQueryRepository;
    private final FranchiseMapper franchiseMapper;
    private final BranchMapper branchMapper;
    private final ProductMapper productMapper;

    @Override
    public Mono<FranchiseAggregate> findFranchiseWithBranchesAndProducts(FranchiseId franchiseId) {
        return franchiseQueryR2dbcRepository
                .findFranchiseWithBranchesAndProducts(franchiseId.getValue().toString())
                .collectList()
                .map(list -> FranchiseAggregateFactory.fromRawData(list));
    }

    @Override
    public Flux<ProductMaxStock> findProductsWithMaxStockByFranchise(FranchiseId franchiseId) {
        log.debug("Finding products with max stock for franchise: {}", franchiseId);

        return productQueryRepository.findProductsWithMaxStockByFranchise(String.valueOf(franchiseId.getValue()))
                .map(this::mapProjectionToProductMaxStock)
                .doOnComplete(() -> log.debug("Products with max stock retrieved for franchise: {}", franchiseId));
    }


    private ProductMaxStock mapProjectionToProductMaxStock(ProductMaxStockProjection projection) {
        return ProductMaxStock.builder()
                .productId(ProductId.of(projection.getId()))
                .productName(projection.getName())
                .branchId(BranchId.of(projection.getBranch_id()))
                .branchName(projection.getBranch_name())
                .stock(projection.getStock())
                .build();
    }
}