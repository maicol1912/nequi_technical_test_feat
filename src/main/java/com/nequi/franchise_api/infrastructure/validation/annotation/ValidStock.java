package com.nequi.franchise_api.infrastructure.validation.annotation;

import com.nequi.franchise_api.infrastructure.validation.validator.StockValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = StockValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStock {

    String message() default "Invalid stock value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
