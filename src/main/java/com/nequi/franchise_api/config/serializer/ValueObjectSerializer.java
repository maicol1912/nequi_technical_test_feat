package com.nequi.franchise_api.config.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ValueObjectSerializer extends JsonSerializer<ValueObject<?>> {

    @Override
    public void serialize(ValueObject<?> valueObject, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (valueObject == null) {
            gen.writeNull();
            return;
        }
        Object value = valueObject.getValue();
        if (value == null) {
            gen.writeNull();
        } else if (value instanceof String) {
            gen.writeString((String) value);
        } else if (value instanceof Number) {
            gen.writeNumber(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            gen.writeBoolean((Boolean) value);
        } else {
            gen.writeObject(value); // fallback
        }
    }
}

