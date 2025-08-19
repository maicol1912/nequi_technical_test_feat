package com.nequi.franchise_api.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateFranchiseException extends DomainException {

    private static final String ERROR_CODE = "DUPLICATE_FRANCHISE";

    public DuplicateFranchiseException(String franchiseName) {
        super(String.format("Franchise already exists with name: %s", franchiseName));
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}