package com.nequi.franchise_api.application.event;

import com.nequi.franchise_api.domain.model.event.FranchiseCreatedEvent;
import com.nequi.franchise_api.domain.model.event.BranchAddedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FranchiseEventHandler {

    @Async
    @EventListener
    public void handleFranchiseCreated(FranchiseCreatedEvent event) {
        log.info("Processing FranchiseCreatedEvent: franchiseId={}, name={}, timestamp={}",
                event.getFranchiseId(), event.getFranchiseName(), event.getTimestamp());

        Mono.fromRunnable(() -> {
                    log.debug("Franchise created event processed successfully for franchise: {}",
                            event.getFranchiseId());
                })
                .doOnError(error -> log.error("Error processing FranchiseCreatedEvent: {}", error.getMessage()))
                .subscribe();
    }

    @Async
    @EventListener
    public void handleBranchAdded(BranchAddedEvent event) {
        log.info("Processing BranchAddedEvent: branchId={}, franchiseId={}, name={}, timestamp={}",
                event.getBranchId(), event.getFranchiseId(), event.getBranchName(), event.getTimestamp());

        Mono.fromRunnable(() -> {
                    log.debug("Branch added event processed successfully for branch: {}",
                            event.getBranchId());
                })
                .doOnError(error -> log.error("Error processing BranchAddedEvent: {}", error.getMessage()))
                .subscribe();
    }
}