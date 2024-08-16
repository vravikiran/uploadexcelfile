package com.app.service.customer.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.service.customer.services.DBConfig;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
/**
 * Checks given GreetingType is valid
 */
public class GreetingTypeValidator implements ConstraintValidator<IsValidGreetingType, String> {
	@Autowired
	DBConfig dbConfig;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return dbConfig.getGreetings().containsKey(value.toUpperCase());
	}

}
