package com.nequi.franchise_api.infrastructure.adapter.out.persistence.adapter;

import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.domain.port.out.BranchRepository;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository.BranchR2dbcRepository;
import com.nequi.franchise_api.infrastructure.mapper.BranchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchRepository {

    private final BranchR2dbcRepository branchR2dbcRepository;
    private final BranchMapper branchMapper;

    @Override
    public Mono<Branch> save(Branch branch) {
        log.debug("Saving branch: {}", branch.getId());
        return Mono.fromCallable(() -> branchMapper.toEntity(branch))
                .flatMap(branchR2dbcRepository::save)
                .map(branchMapper::toDomain)
                .doOnSuccess(saved -> log.debug("Branch saved: {}", saved.getId()));
    }

    @Override
    public Mono<Branch> findById(BranchId branchId) {
        log.debug("Finding branch by ID: {}", branchId);
        return branchR2dbcRepository.findById(branchId.getValue())
                .map(branchMapper::toDomain)
                .doOnNext(found -> log.debug("Branch found: {}", found.getId()));
    }

    @Override
    public Flux<Branch> findByFranchiseId(FranchiseId franchiseId) {
        log.debug("Finding branches by franchise ID: {}", franchiseId);
        return branchR2dbcRepository.findByFranchiseIdOrderByName(franchiseId.getValue())
                .map(branchMapper::toDomain)
                .doOnComplete(() -> log.debug("Branches retrieved for franchise: {}", franchiseId));
    }

    @Override
    public Flux<Branch> findAll() {
        log.debug("Finding all branches");
        return branchR2dbcRepository.findAll()
                .map(branchMapper::toDomain)
                .doOnComplete(() -> log.debug("All branches retrieved"));
    }

    @Override
    public Mono<Boolean> existsById(BranchId branchId) {
        log.debug("Checking if branch exists: {}", branchId);
        return branchR2dbcRepository.existsById(branchId.getValue());
    }

    @Override
    public Mono<Boolean> existsByFranchiseIdAndName(FranchiseId franchiseId, String name) {
        log.debug("Checking if branch exists by franchise ID and name: {} - {}", franchiseId, name);
        return branchR2dbcRepository.existsByFranchiseIdAndNameIgnoreCase(franchiseId.getValue(), name);
    }

    @Override
    public Mono<Void> deleteById(BranchId branchId) {
        log.debug("Deleting branch: {}", branchId);
        return branchR2dbcRepository.deleteById(branchId.getValue())
                .doOnSuccess(unused -> log.debug("Branch deleted: {}", branchId));
    }

    @Override
    public Mono<Void> deleteByFranchiseId(FranchiseId franchiseId) {
        log.debug("Deleting branches by franchise ID: {}", franchiseId);
        return branchR2dbcRepository.deleteByFranchiseId(franchiseId.getValue())
                .doOnSuccess(unused -> log.debug("Branches deleted for franchise: {}", franchiseId));
    }
}
