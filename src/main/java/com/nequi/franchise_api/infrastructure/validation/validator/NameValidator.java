package com.nequi.franchise_api.infrastructure.validation.validator;

import com.nequi.franchise_api.infrastructure.validation.annotation.ValidName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s\\-_.&'()]+$");
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;

    @Override
    public void initialize(ValidName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        String trimmedValue = value.trim();

        if (trimmedValue.length() < MIN_LENGTH || trimmedValue.length() > MAX_LENGTH) {
            addConstraintViolation(context,
                    String.format("Name must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH));
            return false;
        }

        if (!NAME_PATTERN.matcher(trimmedValue).matches()) {
            addConstraintViolation(context, "Name contains invalid characters");
            return false;
        }

        if (trimmedValue.contains("  ")) {
            addConstraintViolation(context, "Name cannot contain consecutive spaces");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
