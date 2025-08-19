package com.nequi.franchise_api.domain.model.valueobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nequi.franchise_api.config.serializer.ValueObject;
import com.nequi.franchise_api.config.serializer.ValueObjectSerializer;
import lombok.Value;

@Value
@JsonSerialize(using = ValueObjectSerializer.class)
public class Name implements ValueObject<String> {
    String value;

    public static Name of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        String trimmed = value.trim();
        if (trimmed.length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters long");
        }

        if (trimmed.length() > 100) {
            throw new IllegalArgumentException("Name must not exceed 100 characters");
        }

        return new Name(trimmed);
    }

    @Override
    public String toString() {
        return value;
    }
}
