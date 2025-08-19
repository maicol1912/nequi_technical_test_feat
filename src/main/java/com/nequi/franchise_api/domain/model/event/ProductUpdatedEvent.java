package com.nequi.franchise_api.domain.model.event;

import com.nequi.franchise_api.domain.model.valueobject.ProductId;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Data
public class ProductUpdatedEvent extends ApplicationEvent {

    public final ProductId productId;
    public final String oldName;
    public final String newName;
    public final Integer oldStock;
    public final Integer newStock;
    public final LocalDateTime occurredAt;

    public ProductUpdatedEvent(Object source, ProductId productId, String oldName, String newName,
                               Integer oldStock, Integer newStock) {
        super(source);
        this.productId = productId;
        this.oldName = oldName;
        this.newName = newName;
        this.oldStock = oldStock;
        this.newStock = newStock;
        this.occurredAt = LocalDateTime.now();
    }
}
