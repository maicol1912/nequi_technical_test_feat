package com.nequi.franchise_api.infrastructure.validation.validator;

import com.nequi.franchise_api.infrastructure.validation.annotation.ValidStock;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StockValidator implements ConstraintValidator<ValidStock, Integer> {

    private static final int MIN_STOCK = 0;
    private static final int MAX_STOCK = 1_000_000;

    @Override
    public void initialize(ValidStock constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value < MIN_STOCK) {
            addConstraintViolation(context, "Stock cannot be negative");
            return false;
        }

        if (value > MAX_STOCK) {
            addConstraintViolation(context,
                    String.format("Stock cannot exceed %,d units", MAX_STOCK));
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
