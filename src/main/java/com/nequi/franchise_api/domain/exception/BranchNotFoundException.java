package com.nequi.franchise_api.domain.exception;

import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BranchNotFoundException extends DomainException {

    private static final String ERROR_CODE = "BRANCH_NOT_FOUND";

    public BranchNotFoundException(BranchId branchId) {
        super(String.format("Branch not found with ID: %s", branchId.getValue()));
    }

    public BranchNotFoundException(String branchId) {
        super(String.format("Branch not found with ID: %s", branchId));
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }
}
