package com.nequi.franchise_api.domain.model.valueobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nequi.franchise_api.config.serializer.ValueObject;
import com.nequi.franchise_api.config.serializer.ValueObjectSerializer;
import lombok.Value;
import java.util.UUID;

@Value
@JsonSerialize(using = ValueObjectSerializer.class)
public class FranchiseId implements ValueObject<UUID> {
    UUID value;

    public static FranchiseId of(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("Franchise ID cannot be null");
        }
        return new FranchiseId(value);
    }

    public static FranchiseId of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Franchise ID cannot be null or empty");
        }
        try {
            return new FranchiseId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Franchise ID format: " + value);
        }
    }

    public static FranchiseId generate() {
        return new FranchiseId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}