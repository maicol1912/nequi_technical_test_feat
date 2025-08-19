package com.nequi.franchise_api.application.service;

import com.nequi.franchise_api.domain.exception.BranchNotFoundException;
import com.nequi.franchise_api.domain.exception.FranchiseNotFoundException;
import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.event.BranchAddedEvent;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.domain.model.valueobject.Name;
import com.nequi.franchise_api.domain.port.in.command.BranchCommandUseCase;
import com.nequi.franchise_api.domain.port.in.query.BranchQueryUseCase;
import com.nequi.franchise_api.domain.port.out.BranchRepository;
import com.nequi.franchise_api.domain.port.out.EventPublisher;
import com.nequi.franchise_api.domain.port.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchApplicationService implements
        BranchCommandUseCase,
        BranchQueryUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;
    private final EventPublisher eventPublisher;

    @Override
    public Mono<Branch> addBranch(AddBranchCommand command) {
        log.debug("Adding branch to franchise {} with name: {}", command.franchiseId(), command.name());

        FranchiseId franId = FranchiseId.of(command.franchiseId());
        Name branchName = Name.of(command.name());

        return franchiseRepository.existsById(franId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new FranchiseNotFoundException(franId));
                    }

                    return branchRepository.existsByFranchiseIdAndName(franId, command.name())
                            .flatMap(branchExists -> {
                                if (branchExists) {
                                    return Mono.error(new IllegalArgumentException("Branch with name '" + command.name() + "' already exists in this franchise"));
                                }

                                Branch branch = Branch.create(franId, branchName);

                                return branchRepository.save(branch)
                                        .doOnSuccess(savedBranch -> {
                                            log.info("Branch added successfully: {}", savedBranch.getId());
                                            eventPublisher.publishEvent(
                                                    new BranchAddedEvent(this, franId, savedBranch.getId(), savedBranch.getName().getValue())
                                            );
                                        });
                            });
                });
    }

    @Override
    public Mono<Branch> updateBranch(UpdateBranchCommand command) {
        log.debug("Updating branch {} with name: {}", command.branchId(), command.name());

        BranchId id = BranchId.of(command.branchId());
        Name newName = Name.of(command.name());

        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(id)))
                .flatMap(branch -> {
                    branch.updateName(newName);
                    return branchRepository.save(branch);
                })
                .doOnSuccess(updatedBranch ->
                        log.info("Branch updated successfully: {}", updatedBranch.getId())
                );
    }

    @Override
    public Mono<Branch> getBranch(GetBranchQuery query) {
        log.debug("Getting branch: {}", query.branchId());

        BranchId id = BranchId.of(query.branchId());

        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(id)))
                .doOnSuccess(branch -> log.debug("Branch found: {}", branch.getId()));

    }

    @Override
    public Flux<Branch> getBranchesByFranchise(GetBranchesByFranchiseQuery query) {
        log.debug("Getting franchiseId: {}", query.franchiseId());

        FranchiseId id = FranchiseId.of(query.franchiseId());

        return branchRepository.findByFranchiseId(id)
                .switchIfEmpty(Flux.error(new FranchiseNotFoundException(id)))
                .doOnNext(branchByFranchise -> log.debug("Branch by franchise found: {}", branchByFranchise.getId()));
    }
}
