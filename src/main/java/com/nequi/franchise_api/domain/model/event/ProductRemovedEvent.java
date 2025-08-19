package com.nequi.franchise_api.domain.model.event;

import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.ProductId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ProductRemovedEvent extends ApplicationEvent {

    private final BranchId branchId;
    private final ProductId productId;
    private final String productName;
    private final Integer finalStock;
    private final LocalDateTime occurredAt;

    public ProductRemovedEvent(Object source, BranchId branchId, ProductId productId,
                               String productName, Integer finalStock) {
        super(source);
        this.branchId = branchId;
        this.productId = productId;
        this.productName = productName;
        this.finalStock = finalStock;
        this.occurredAt = LocalDateTime.now();
    }
}
