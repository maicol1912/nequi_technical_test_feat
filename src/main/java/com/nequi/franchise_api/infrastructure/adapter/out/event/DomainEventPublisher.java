package com.nequi.franchise_api.infrastructure.adapter.out.event;

import com.nequi.franchise_api.domain.port.out.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Slf4j
@Component
public class DomainEventPublisher implements EventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(DomainEventPublisher.class);

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public DomainEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publishEvent(Object event) {
        try {
            eventPublisher.publishEvent(event);
            logger.info("Published event: {}", event);
        } catch (Exception e) {
            logger.error("Failed to publish event: {}", event, e);
            throw e; // Re-lanzar la excepción si es necesario
        }
    }
}
