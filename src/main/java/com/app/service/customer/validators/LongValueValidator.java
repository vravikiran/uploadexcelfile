package com.app.service.customer.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
/**
 * Checks given longValue is null or empty
 */
public class LongValueValidator implements ConstraintValidator<IsLongNullOrEmpty, Long>{

	public boolean isValid(Long value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		return value == null || Long.toString(value).length() ==0;
	}
}
