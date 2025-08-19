package com.nequi.franchise_api.domain.model.valueobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nequi.franchise_api.config.serializer.ValueObject;
import com.nequi.franchise_api.config.serializer.ValueObjectSerializer;
import lombok.Value;

import java.util.UUID;

@Value
@JsonSerialize(using = ValueObjectSerializer.class)
public class ProductId implements ValueObject<UUID> {
    UUID value;

    public static ProductId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        return new ProductId(value);
    }

    public static ProductId of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        try {
            return new ProductId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Product ID format: " + value);
        }
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
