package com.nequi.franchise_api.domain.model.event;

import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.ProductId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ProductAddedEvent extends ApplicationEvent {

    private final BranchId branchId;
    private final ProductId productId;
    private final String productName;
    private final Integer initialStock;
    private final LocalDateTime occurredAt;

    public ProductAddedEvent(Object source, BranchId branchId, ProductId productId,
                             String productName, Integer initialStock) {
        super(source);
        this.branchId = branchId;
        this.productId = productId;
        this.productName = productName;
        this.initialStock = initialStock;
        this.occurredAt = LocalDateTime.now();
    }
}
