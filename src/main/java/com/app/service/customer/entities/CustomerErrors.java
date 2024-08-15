package com.app.service.customer.entities;
import java.util.Map;

public class CustomerErrors {
	private CustomerDto customerDto;
	private Map<String,String> errors;
	public CustomerDto getCustomerDto() {
		return customerDto;
	}
	public void setCustomerDto(CustomerDto customerDto) {
		this.customerDto = customerDto;
	}
	public Map<String, String> getErrors() {
		return errors;
	}
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
}
