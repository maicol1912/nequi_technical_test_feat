package com.nequi.franchise_api.domain.model.valueobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nequi.franchise_api.config.serializer.ValueObject;
import com.nequi.franchise_api.config.serializer.ValueObjectSerializer;
import lombok.Value;

import java.util.UUID;

@Value
@JsonSerialize(using = ValueObjectSerializer.class)
public class BranchId implements ValueObject<UUID> {
    UUID value;

    public static BranchId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Branch ID cannot be null");
        }
        return new BranchId(value);
    }

    public static BranchId of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Branch ID cannot be null or empty");
        }
        try {
            return new BranchId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Branch ID format: " + value);
        }
    }

    public static BranchId generate() {
        return new BranchId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
