package com.nequi.franchise_api.domain.service.query;

import com.nequi.franchise_api.domain.exception.FranchiseNotFoundException;
import com.nequi.franchise_api.domain.model.aggregate.FranchiseAggregate;
import com.nequi.franchise_api.domain.model.aggregate.ProductMaxStock;
import com.nequi.franchise_api.domain.model.entity.Franchise;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.domain.port.out.FranchiseQueryRepository;
import com.nequi.franchise_api.domain.port.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FranchiseQueryService {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseQueryRepository franchiseQueryRepository;

    public Mono<Franchise> getFranchise(String franchiseId) {
        log.debug("Getting franchise: {}", franchiseId);

        FranchiseId id = FranchiseId.of(franchiseId);

        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)))
                .doOnSuccess(franchise -> log.debug("Franchise found: {}", franchise.getId()));
    }

    public Flux<Franchise> getAllFranchises() {
        log.debug("Getting all franchises");

        return franchiseRepository.findAll()
                .doOnComplete(() -> log.debug("All franchises retrieved"));
    }

    public Mono<FranchiseAggregate> getFranchiseWithBranchesAndProducts(String franchiseId) {
        log.debug("Getting franchise with branches and products: {}", franchiseId);

        FranchiseId id = FranchiseId.of(franchiseId);

        return franchiseRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new FranchiseNotFoundException(id));
                    }

                    return franchiseQueryRepository.findFranchiseWithBranchesAndProducts(id)
                            .doOnSuccess(aggregate ->
                                    log.debug("Franchise aggregate found with {} branches and {} products",
                                            aggregate.getTotalBranches(), aggregate.getTotalBranches())
                            );
                });
    }

    public Flux<ProductMaxStock> getProductsWithMaxStock(String franchiseId) {
        log.debug("Getting products with max stock for franchise: {}", franchiseId);

        FranchiseId id = FranchiseId.of(franchiseId);

        return franchiseRepository.existsById(id)
                .flatMapMany(exists -> {
                    if (!exists) {
                        return Flux.error(new FranchiseNotFoundException(id));
                    }

                    return franchiseQueryRepository.findProductsWithMaxStockByFranchise(id)
                            .doOnComplete(() -> log.debug("Products with max stock retrieved for franchise: {}", franchiseId));
                });
    }
}
