package com.nequi.franchise_api.application.service;

import com.nequi.franchise_api.domain.exception.DuplicateFranchiseException;
import com.nequi.franchise_api.domain.exception.FranchiseNotFoundException;
import com.nequi.franchise_api.domain.model.aggregate.FranchiseAggregate;
import com.nequi.franchise_api.domain.model.aggregate.ProductMaxStock;
import com.nequi.franchise_api.domain.model.entity.Franchise;
import com.nequi.franchise_api.domain.model.event.FranchiseCreatedEvent;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.domain.model.valueobject.Name;
import com.nequi.franchise_api.domain.port.in.command.FranchiseCommandUseCase;
import com.nequi.franchise_api.domain.port.in.query.FranchiseQueryUseCase;
import com.nequi.franchise_api.domain.port.in.query.ProductsWithMaxStockQueryUseCase;
import com.nequi.franchise_api.domain.port.out.EventPublisher;
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
public class FranchiseApplicationService implements
        FranchiseCommandUseCase,
        FranchiseQueryUseCase,
        ProductsWithMaxStockQueryUseCase {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseQueryRepository franchiseQueryRepository;
    private final EventPublisher eventPublisher;


    @Override
    public Mono<Franchise> createFranchise(CreateFranchiseCommand command) {
        log.debug("Creating franchise with name: {}", command.name());

        Name franchiseName = Name.of(command.name());

        return franchiseRepository.existsByName(command.name())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new DuplicateFranchiseException(command.name()));
                    }

                    Franchise franchise = Franchise.create(franchiseName);

                    return franchiseRepository.save(franchise)
                            .doOnSuccess(savedFranchise -> {
                                log.info("Franchise created successfully: {}", savedFranchise.getId());
                                eventPublisher.publishEvent(
                                        new FranchiseCreatedEvent(this, savedFranchise.getId(), savedFranchise.getName().getValue())
                                );
                            });
                });
    }

    @Override
    public Mono<Franchise> updateFranchise(UpdateFranchiseCommand command) {
        log.debug("Updating franchise {} with name: {}", command.franchiseId(), command.name());

        FranchiseId id = FranchiseId.of(command.franchiseId());
        Name newName = Name.of(command.name());

        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)))
                .flatMap(franchise -> {
                    franchise.updateName(newName);
                    return franchiseRepository.save(franchise);
                })
                .doOnSuccess(updatedFranchise ->
                        log.info("Franchise updated successfully: {}", updatedFranchise.getId())
                );
    }

    @Override
    public Mono<Franchise> getFranchise(GetFranchiseQuery query) {
        log.debug("Getting franchise: {}", query.franchiseId());

        FranchiseId id = FranchiseId.of(query.franchiseId());

        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)))
                .doOnSuccess(franchise -> log.debug("Franchise found: {}", franchise.getId()));
    }

    @Override
    public Flux<Franchise> getAllFranchises() {
        log.debug("Getting all franchises");

        return franchiseRepository.findAll()
                .doOnComplete(() -> log.debug("All franchises retrieved"));
    }

    public Mono<FranchiseAggregate> getFranchiseComplete(String franchiseId) {
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

    @Override
    public Flux<ProductMaxStock> getProductsWithMaxStock(GetProductsWithMaxStockQuery query) {
        log.debug("Getting products with max stock for franchise: {}", query.franchiseId());

        FranchiseId id = FranchiseId.of(query.franchiseId());

        return franchiseRepository.existsById(id)
                .flatMapMany(exists -> {
                    if (!exists) {
                        return Flux.error(new FranchiseNotFoundException(id));
                    }

                    return franchiseQueryRepository.findProductsWithMaxStockByFranchise(id)
                            .doOnComplete(() -> log.debug("Products with max stock retrieved for franchise: {}", query.franchiseId()));
                });
    }
}

