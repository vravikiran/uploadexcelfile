package com.app.service.customer.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
/**
 * Checks given phone number is valid
 */
public class PhoneNumberValidator  implements ConstraintValidator<IsValidPhoneNumber, Long>{

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return Long.toString(value).length() < 10;
	}

}
