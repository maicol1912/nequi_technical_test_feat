package com.nequi.franchise_api.domain.port.out;

import reactor.core.publisher.Mono;

public interface EventPublisher {

    void publishEvent(Object event);
}
