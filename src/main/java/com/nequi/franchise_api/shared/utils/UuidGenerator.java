package com.nequi.franchise_api.shared.utils;

import java.util.UUID;


@FunctionalInterface
public interface UuidGenerator {
    UUID generate();
}
