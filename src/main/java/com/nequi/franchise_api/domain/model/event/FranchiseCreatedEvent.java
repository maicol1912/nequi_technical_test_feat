package com.nequi.franchise_api.domain.model.event;

import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class FranchiseCreatedEvent extends ApplicationEvent {

    private final FranchiseId franchiseId;
    private final String franchiseName;
    private final LocalDateTime occurredAt;

    public FranchiseCreatedEvent(Object source, FranchiseId franchiseId, String franchiseName) {
        super(source);
        this.franchiseId = franchiseId;
        this.franchiseName = franchiseName;
        this.occurredAt = LocalDateTime.now();
    }
}