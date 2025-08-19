package com.nequi.franchise_api.domain.service.command;

import com.nequi.franchise_api.domain.exception.BranchNotFoundException;
import com.nequi.franchise_api.domain.exception.FranchiseNotFoundException;
import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.event.BranchAddedEvent;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.domain.model.valueobject.Name;
import com.nequi.franchise_api.domain.port.out.BranchRepository;
import com.nequi.franchise_api.domain.port.out.EventPublisher;
import com.nequi.franchise_api.domain.port.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchCommandService {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;
    private final EventPublisher eventPublisher;

    public Mono<Branch> addBranch(String franchiseId, String name) {
        log.debug("Adding branch to franchise {} with name: {}", franchiseId, name);

        FranchiseId franId = FranchiseId.of(franchiseId);
        Name branchName = Name.of(name);

        return franchiseRepository.existsById(franId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new FranchiseNotFoundException(franId));
                    }

                    return branchRepository.existsByFranchiseIdAndName(franId, name)
                            .flatMap(branchExists -> {
                                if (branchExists) {
                                    return Mono.error(new IllegalArgumentException("Branch with name '" + name + "' already exists in this franchise"));
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

    public Mono<Branch> updateBranch(String branchId, String name) {
        log.debug("Updating branch {} with name: {}", branchId, name);

        BranchId id = BranchId.of(branchId);
        Name newName = Name.of(name);

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
}
