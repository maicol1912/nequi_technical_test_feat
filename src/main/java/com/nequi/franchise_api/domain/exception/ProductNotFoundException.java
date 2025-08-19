package com.nequi.franchise_api.domain.exception;

import com.nequi.franchise_api.domain.model.valueobject.ProductId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends DomainException {

    private static final String ERROR_CODE = "PRODUCT_NOT_FOUND";

    public ProductNotFoundException(ProductId productId) {
        super(String.format("Product not found with ID: %s", productId.getValue()));
    }

    public ProductNotFoundException(String productId) {
        super(String.format("Product not found with ID: %s", productId));
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}
