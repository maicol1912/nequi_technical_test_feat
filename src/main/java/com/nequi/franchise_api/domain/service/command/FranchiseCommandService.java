package com.nequi.franchise_api.domain.service.command;

import com.nequi.franchise_api.domain.exception.DuplicateFranchiseException;
import com.nequi.franchise_api.domain.exception.FranchiseNotFoundException;
import com.nequi.franchise_api.domain.model.entity.Franchise;
import com.nequi.franchise_api.domain.model.event.FranchiseCreatedEvent;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.domain.model.valueobject.Name;
import com.nequi.franchise_api.domain.port.out.EventPublisher;
import com.nequi.franchise_api.domain.port.out.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FranchiseCommandService {

    private final FranchiseRepository franchiseRepository;
    private final EventPublisher eventPublisher;

    public Mono<Franchise> createFranchise(String name) {
        log.debug("Creating franchise with name: {}", name);

        Name franchiseName = Name.of(name);

        return franchiseRepository.existsByName(name)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new DuplicateFranchiseException(name));
                    }

                    Franchise franchise = Franchise.create(franchiseName);

                    return franchiseRepository.save(franchise)
                            .doOnSuccess(savedFranchise -> {
                                log.info("Franchise created successfully: {}", savedFranchise.getId());
                                eventPublisher.publishEvent(
                                        new FranchiseCreatedEvent(this, savedFranchise.getId(), savedFranchise.getName().getValue())
                                );
                            });
                });
    }

    public Mono<Franchise> updateFranchise(String franchiseId, String name) {
        log.debug("Updating franchise {} with name: {}", franchiseId, name);

        FranchiseId id = FranchiseId.of(franchiseId);
        Name newName = Name.of(name);

        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(id)))
                .flatMap(franchise -> {
                    franchise.updateName(newName);
                    return franchiseRepository.save(franchise);
                })
                .doOnSuccess(updatedFranchise ->
                        log.info("Franchise updated successfully: {}", updatedFranchise.getId())
                );
    }
}
