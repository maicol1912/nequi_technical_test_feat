package com.nequi.franchise_api.domain.port.out;

import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {

    Mono<Branch> save(Branch branch);

    Mono<Branch> findById(BranchId branchId);

    Flux<Branch> findByFranchiseId(FranchiseId franchiseId);

    Flux<Branch> findAll();

    Mono<Boolean> existsById(BranchId branchId);

    Mono<Boolean> existsByFranchiseIdAndName(FranchiseId franchiseId, String name);

    Mono<Void> deleteById(BranchId branchId);

    Mono<Void> deleteByFranchiseId(FranchiseId franchiseId);
}
