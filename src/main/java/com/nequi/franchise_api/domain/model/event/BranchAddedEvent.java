package com.nequi.franchise_api.domain.model.event;

import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class BranchAddedEvent extends ApplicationEvent {

    private final FranchiseId franchiseId;
    private final BranchId branchId;
    private final String branchName;
    private final LocalDateTime occurredAt;

    public BranchAddedEvent(Object source, FranchiseId franchiseId, BranchId branchId, String branchName) {
        super(source);
        this.franchiseId = franchiseId;
        this.branchId = branchId;
        this.branchName = branchName;
        this.occurredAt = LocalDateTime.now();
    }
}
