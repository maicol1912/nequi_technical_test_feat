package com.nequi.franchise_api.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidStockException extends DomainException {

    private static final String ERROR_CODE = "INVALID_STOCK";

    public InvalidStockException(String message) {
        super(message);
    }

    public InvalidStockException(int currentStock, int requestedQuantity) {
        super(String.format("Insufficient stock. Current: %d, Requested: %d", currentStock, requestedQuantity));
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}