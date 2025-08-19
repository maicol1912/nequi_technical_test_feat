package com.nequi.franchise_api.infrastructure.adapter.out.persistence.adapter;

import com.nequi.franchise_api.domain.model.entity.Franchise;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.domain.port.out.FranchiseRepository;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository.FranchiseR2dbcRepository;
import com.nequi.franchise_api.infrastructure.mapper.FranchiseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepository {

    private final FranchiseR2dbcRepository franchiseR2dbcRepository;
    private final FranchiseMapper franchiseMapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        log.debug("Saving franchise: {}", franchise.getId());
        return Mono.fromCallable(() -> franchiseMapper.toEntity(franchise))
                .flatMap(franchiseR2dbcRepository::save)
                .map(franchiseMapper::toDomain)
                .doOnSuccess(saved -> log.debug("Franchise saved: {}", saved.getId()));
    }

    @Override
    public Mono<Franchise> findById(FranchiseId franchiseId) {
        log.debug("Finding franchise by ID: {}", franchiseId);
        return franchiseR2dbcRepository.findById(franchiseId.getValue())
                .map(franchiseMapper::toDomain)
                .doOnNext(found -> log.debug("Franchise found: {}", found.getId()));
    }

    @Override
    public Flux<Franchise> findAll() {
        log.debug("Finding all franchises");
        return franchiseR2dbcRepository.findAll()
                .map(franchiseMapper::toDomain)
                .doOnComplete(() -> log.debug("All franchises retrieved"));
    }

    @Override
    public Mono<Boolean> existsById(FranchiseId franchiseId) {
        log.debug("Checking if franchise exists: {}", franchiseId);
        return franchiseR2dbcRepository.existsById(franchiseId.getValue());
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        log.debug("Checking if franchise exists by name: {}", name);
        return franchiseR2dbcRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public Mono<Void> deleteById(FranchiseId franchiseId) {
        log.debug("Deleting franchise: {}", franchiseId);
        return franchiseR2dbcRepository.deleteById(franchiseId.getValue())
                .doOnSuccess(unused -> log.debug("Franchise deleted: {}", franchiseId));
    }
}