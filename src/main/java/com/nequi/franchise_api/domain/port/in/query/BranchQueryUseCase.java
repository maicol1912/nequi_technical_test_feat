package com.nequi.franchise_api.domain.port.in.query;

import com.nequi.franchise_api.domain.model.entity.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchQueryUseCase {

    Mono<Branch> getBranch(GetBranchQuery query);

    Flux<Branch> getBranchesByFranchise(GetBranchesByFranchiseQuery query);

    record GetBranchQuery(String branchId) {
        public GetBranchQuery {
            if (branchId == null || branchId.trim().isEmpty()) {
                throw new IllegalArgumentException("Branch ID cannot be null or empty");
            }
        }
    }

    record GetBranchesByFranchiseQuery(String franchiseId) {
        public GetBranchesByFranchiseQuery {
            if (franchiseId == null || franchiseId.trim().isEmpty()) {
                throw new IllegalArgumentException("Franchise ID cannot be null or empty");
            }
        }
    }
}