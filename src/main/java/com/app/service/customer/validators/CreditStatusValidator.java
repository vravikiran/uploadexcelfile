package com.app.service.customer.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.service.customer.services.DBConfig;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreditStatusValidator implements ConstraintValidator<IsValidCreditStatus, String> {
	
	@Autowired
	private DBConfig dbConfig;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return dbConfig.getCreditStatuses().containsKey(value.toUpperCase());
	}
}
