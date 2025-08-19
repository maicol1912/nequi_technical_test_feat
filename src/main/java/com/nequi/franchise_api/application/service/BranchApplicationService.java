package com.nequi.franchise_api.application.service;

import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.port.in.command.BranchCommandUseCase;
import com.nequi.franchise_api.domain.port.in.query.BranchQueryUseCase;
import com.nequi.franchise_api.domain.service.command.BranchCommandService;
import com.nequi.franchise_api.domain.service.query.BranchQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BranchApplicationService implements
        BranchCommandUseCase,
        BranchQueryUseCase {

    private final BranchCommandService branchCommandService;
    private final BranchQueryService branchQueryService;

    @Override
    public Mono<Branch> addBranch(AddBranchCommand command) {
        return branchCommandService.addBranch(command.franchiseId(), command.name());
    }

    @Override
    public Mono<Branch> updateBranch(UpdateBranchCommand command) {
        return branchCommandService.updateBranch(command.branchId(), command.name());
    }

    @Override
    public Mono<Branch> getBranch(GetBranchQuery query) {
        return branchQueryService.getBranch(query.branchId());
    }

    @Override
    public Flux<Branch> getBranchesByFranchise(GetBranchesByFranchiseQuery query) {
        return branchQueryService.getBranchByFranchise(query.franchiseId());
    }
}
