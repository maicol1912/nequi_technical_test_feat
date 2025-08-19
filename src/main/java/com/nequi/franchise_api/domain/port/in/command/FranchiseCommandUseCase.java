package com.nequi.franchise_api.domain.port.in.command;

import com.nequi.franchise_api.domain.model.entity.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseCommandUseCase {

    Mono<Franchise> createFranchise(CreateFranchiseCommand command);

    Mono<Franchise> updateFranchise(UpdateFranchiseCommand command);

    record CreateFranchiseCommand(String name) {
        public CreateFranchiseCommand {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Franchise name cannot be null or empty");
            }
        }
    }

    record UpdateFranchiseCommand(String franchiseId, String name) {
        public UpdateFranchiseCommand {
            if (franchiseId == null || franchiseId.trim().isEmpty()) {
                throw new IllegalArgumentException("Franchise ID cannot be null or empty");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Franchise name cannot be null or empty");
            }
        }
    }
}