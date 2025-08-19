
package com.nequi.franchise_api.domain.port.in.query;

import com.nequi.franchise_api.domain.model.entity.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseQueryUseCase {

    Flux<Franchise> getAllFranchises();

    Mono<Franchise> getFranchise(GetFranchiseQuery query);

    record GetFranchiseQuery(String franchiseId) {
        public GetFranchiseQuery {
            if (franchiseId == null || franchiseId.trim().isEmpty()) {
                throw new IllegalArgumentException("Franchise ID cannot be null or empty");
            }
        }
    }
}