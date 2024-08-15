package com.app.service.customer.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.service.customer.entities.CustomerNameDto;
import com.app.service.customer.entities.GSTINAndPanDto;
import com.app.service.customer.repositories.CustomerRepository;

@Service
@Async("validateCustFieldsByNamesExecutor")
public class CustomerFieldsByNameValidator {
	@Autowired
	private CustomerRepository customerRepository;

	public CompletableFuture<Boolean> isCustomerNameExists(String name) {
		return CompletableFuture.completedFuture(customerRepository.findCustomerName(name) == null ? true : false);
	}

	public CompletableFuture<Boolean> isCustomerCodeExists(String code) {
		return CompletableFuture.completedFuture(customerRepository.findCustomerCode(code) == null ? true : false);
	}

	public CompletableFuture<Boolean> isCustomerAliasExists(String customerAlias) {
		return CompletableFuture
				.completedFuture(customerRepository.findCustomerAlias(customerAlias) == null ? true : false);
	}

	public CompletableFuture<Boolean> isUniqueCustomerGSTIN(String customerGSTIN, String customerName) {
		return CompletableFuture.completedFuture(
				customerRepository.fetchCustomerGSTINByCustomerName(customerGSTIN, customerName) != null ? false
						: true);
	}

	public CompletableFuture<Boolean> isUniqueSupplyGSTIN(String supplyGSTIN, String customerName) {
		return CompletableFuture.completedFuture(
				customerRepository.fetchSupplyGSTINByCustomerName(supplyGSTIN, customerName) != null ? false : true);
	}

	public CompletableFuture<Boolean> isUniquePAN(String panNo, String customerName) {
		return CompletableFuture.completedFuture(
				customerRepository.fetchPanNoByCustomerName(panNo, customerName) != null ? false : true);
	}
	
	public CompletableFuture<CustomerNameDto> fetchDiffCustomerNames(String customerName,String customerAlias, String customerCode) {
		return CompletableFuture.completedFuture(customerRepository.getDiffCustomerNames(customerName, customerAlias, customerCode));
	}
	
	public CompletableFuture<GSTINAndPanDto> getGSTINAndPANDetails(String customerName,String customerGstIn,String supplyGstIn, String panNo) {
		return CompletableFuture.completedFuture(customerRepository.getGSTINAndPANDetails(customerGstIn, supplyGstIn, panNo, customerName));
	}
	
	public CompletableFuture<UUID> fetchParentCustomerId(String customerName) {
		return CompletableFuture.completedFuture(customerRepository.getParentCustomerId(customerName));
	}

}
