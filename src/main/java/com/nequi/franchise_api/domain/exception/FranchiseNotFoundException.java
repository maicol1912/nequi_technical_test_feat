package com.nequi.franchise_api.domain.exception;

import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FranchiseNotFoundException extends DomainException {

    private static final String ERROR_CODE = "FRANCHISE_NOT_FOUND";

    public FranchiseNotFoundException(FranchiseId franchiseId) {
        super(String.format("Franchise not found with ID: %s", franchiseId.getValue()));
    }

    public FranchiseNotFoundException(String franchiseId) {
        super(String.format("Franchise not found with ID: %s", franchiseId));
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}
