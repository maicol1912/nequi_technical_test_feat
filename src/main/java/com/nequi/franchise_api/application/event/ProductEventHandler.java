package com.nequi.franchise_api.application.event;

import com.nequi.franchise_api.domain.model.event.ProductAddedEvent;
import com.nequi.franchise_api.domain.model.event.ProductUpdatedEvent;
import com.nequi.franchise_api.domain.model.event.ProductRemovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventHandler {

    @Async
    @EventListener
    public void handleProductAdded(ProductAddedEvent event) {
        log.info("Processing ProductAddedEvent: productId={}, branchId={}, timestamp={}",
                event.getProductId(), event.getBranchId(), event.getTimestamp());

        Mono.fromRunnable(() -> {
                    log.debug("Product added event processed successfully for product: {}",
                            event.getProductId());
                })
                .doOnError(error -> log.error("Error processing ProductAddedEvent: {}", error.getMessage()))
                .subscribe();
    }

    @Async
    @EventListener
    public void handleProductUpdated(ProductUpdatedEvent event) {
        log.info("Processing ProductUpdatedEvent: productId={}, nameChange=[{}->{}], stockChange=[{}->{}], timestamp={}",
                event.getProductId(), event.getOldName(), event.getNewName(),
                event.getOldStock(), event.getNewStock(), event.getOccurredAt());

        Mono.fromRunnable(() -> {
                    if (event.getNewStock() != null && event.getNewStock() < 10) {
                        log.warn("Product {} now has low stock: {} units (was {})",
                                event.getProductId(), event.getNewStock(), event.getOldStock());
                        sendLowStockAlert(event.getProductId().toString(), event.getNewStock());
                    }

                    if (!event.getOldName().equals(event.getNewName())) {
                        log.info("Product name changed: {} -> {} for productId: {}",
                                event.getOldName(), event.getNewName(), event.getProductId());
                    }

                    log.debug("Product updated event processed successfully for product: {}",
                            event.getProductId());
                })
                .doOnError(error -> log.error("Error processing ProductUpdatedEvent: {}", error.getMessage()))
                .subscribe();
    }

    @Async
    @EventListener
    public void handleProductRemoved(ProductRemovedEvent event) {
        log.info("Processing ProductRemovedEvent: productId={}, branchId={}, timestamp={}",
                event.getProductId(), event.getBranchId(), event.getTimestamp());

        Mono.fromRunnable(() -> {
                    log.debug("Product removed event processed successfully for product: {}",
                            event.getProductId());
                })
                .doOnError(error -> log.error("Error processing ProductRemovedEvent: {}", error.getMessage()))
                .subscribe();
    }

    private void sendLowStockAlert(String productId, Integer currentStock) {

        log.warn("LOW STOCK ALERT: Product '{}' has only {} units remaining",
                productId, currentStock);
    }

    private void sendLowStockAlert(String productId, String branchId) {
        log.warn("LOW STOCK ALERT: Product '{}' in branch '{}' may have low stock",
                productId, branchId);
    }
}