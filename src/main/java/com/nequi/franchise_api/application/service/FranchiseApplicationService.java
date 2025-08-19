package com.nequi.franchise_api.application.service;

import com.nequi.franchise_api.domain.model.aggregate.FranchiseAggregate;
import com.nequi.franchise_api.domain.model.aggregate.ProductMaxStock;
import com.nequi.franchise_api.domain.model.entity.Franchise;
import com.nequi.franchise_api.domain.port.in.command.FranchiseCommandUseCase;
import com.nequi.franchise_api.domain.port.in.query.FranchiseQueryUseCase;
import com.nequi.franchise_api.domain.port.in.query.ProductsWithMaxStockQueryUseCase;
import com.nequi.franchise_api.domain.service.command.FranchiseCommandService;
import com.nequi.franchise_api.domain.service.query.FranchiseQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FranchiseApplicationService implements
        FranchiseCommandUseCase,
        FranchiseQueryUseCase,
        ProductsWithMaxStockQueryUseCase {

    private final FranchiseCommandService franchiseCommandService;
    private final FranchiseQueryService franchiseQueryService;

    @Override
    public Mono<Franchise> createFranchise(CreateFranchiseCommand command) {
        return franchiseCommandService.createFranchise(command.name());
    }

    @Override
    public Mono<Franchise> updateFranchise(UpdateFranchiseCommand command) {
        return franchiseCommandService.updateFranchise(command.franchiseId(), command.name());
    }

    @Override
    public Mono<Franchise> getFranchise(GetFranchiseQuery query) {
        return franchiseQueryService.getFranchise(query.franchiseId());
    }

    @Override
    public Flux<Franchise> getAllFranchises() {
        return franchiseQueryService.getAllFranchises();
    }

    public Mono<FranchiseAggregate> getFranchiseComplete(String franchiseId) {
        return franchiseQueryService.getFranchiseWithBranchesAndProducts(franchiseId);
    }

    @Override
    public Flux<ProductMaxStock> getProductsWithMaxStock(GetProductsWithMaxStockQuery query) {
        return franchiseQueryService.getProductsWithMaxStock(query.franchiseId());
    }
}

