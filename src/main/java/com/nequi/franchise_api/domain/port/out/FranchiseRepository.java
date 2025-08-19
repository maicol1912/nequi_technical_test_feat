package com.nequi.franchise_api.domain.port.out;

import com.nequi.franchise_api.domain.model.entity.Franchise;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Mono<Franchise> save(Franchise franchise);

    Mono<Franchise> findById(FranchiseId franchiseId);

    Flux<Franchise> findAll();

    Mono<Boolean> existsById(FranchiseId franchiseId);

    Mono<Boolean> existsByName(String name);

    Mono<Void> deleteById(FranchiseId franchiseId);
}
