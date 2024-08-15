package com.app.service.customer.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.service.customer.services.DBConfig;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RatingTypeValidator implements ConstraintValidator<IsValidRatingType, String> {
	@Autowired
	DBConfig dbConfig;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return dbConfig.getRatings().containsKey(value.toUpperCase());
	}

}
