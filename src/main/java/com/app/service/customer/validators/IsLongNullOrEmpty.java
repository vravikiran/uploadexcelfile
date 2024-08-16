package com.app.service.customer.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
/**
 * annotation to check given long value is null or empty
 */
@Target({ ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LongValueValidator.class)
@Documented
public @interface IsLongNullOrEmpty {
	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
