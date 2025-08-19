package com.nequi.franchise_api.domain.service.query;

import com.nequi.franchise_api.domain.exception.BranchNotFoundException;
import com.nequi.franchise_api.domain.exception.FranchiseNotFoundException;
import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.domain.port.out.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchQueryService {

    private final BranchRepository branchRepository;

    public Mono<Branch> getBranch(String branchId) {
        log.debug("Getting branch: {}", branchId);

        BranchId id = BranchId.of(branchId);

        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(id)))
                .doOnSuccess(branch -> log.debug("Branch found: {}", branch.getId()));
    }

    public Flux<Branch> getBranchByFranchise(String franchiseId) {
        log.debug("Getting franchiseId: {}", franchiseId);

        FranchiseId id = FranchiseId.of(franchiseId);

        return branchRepository.findByFranchiseId(id)
                .switchIfEmpty(Flux.error(new FranchiseNotFoundException(id)))
                .doOnNext(branchByFranchise -> log.debug("Branch by franchise found: {}", branchByFranchise.getId()));
    }
}
