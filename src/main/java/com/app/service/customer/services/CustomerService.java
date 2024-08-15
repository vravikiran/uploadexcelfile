package com.app.service.customer.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.service.customer.entities.Customer;
import com.app.service.customer.entities.CustomerDto;
import com.app.service.customer.enums.CustomerCSVFileHeaders;
import com.app.service.customer.repositories.BrandRepository;
import com.app.service.customer.repositories.CustomerRepository;

@Service
public class CustomerService {
	@Autowired
	CustomerFieldsValidator customerCodeValidator;
	@Autowired
	ErrorLogService errorLogService;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	DBConfig dbConfig;
	@Autowired
	BrandRepository brandRepository;

	public void uploadCustomerInfo(InputStream in) throws Exception {
		List<CustomerDto> customerDtos = null;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		try {
			CSVParser csvParser = CSVParser.parse(bufferedReader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(','));
			List<CSVRecord> csvRecords = csvParser.getRecords();
			if (csvRecords.isEmpty()) {
				throw new Exception("No data present in a given file");
			}
			customerDtos = csvRecords.stream().map(customer -> convertCsvRecordToCustomerRecord(customer))
					.collect(Collectors.toList());
			validateListOfCustomers(customerDtos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private CustomerDto convertCsvRecordToCustomerRecord(CSVRecord csvRecord) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setParentCompany(csvRecord.get(CustomerCSVFileHeaders.ParentCompany));
		// customerDto.setDataImportFk(csvRecord.get(CustomerCSVFileHeaders.DataImportfk));
		customerDto.setCustomerCode(csvRecord.get(CustomerCSVFileHeaders.CustomerCode));
		customerDto.setCustomerName(csvRecord.get(CustomerCSVFileHeaders.CustomerName));
		customerDto.setCustomerAlias(csvRecord.get(CustomerCSVFileHeaders.CustomerAlias));
		customerDto.setCustomerType(csvRecord.get(CustomerCSVFileHeaders.CustomerType));
		customerDto.setBrand(csvRecord.get(CustomerCSVFileHeaders.Brand));
		customerDto.setSupplyState(csvRecord.get(CustomerCSVFileHeaders.SupplyState));
		customerDto.setGstType(csvRecord.get(CustomerCSVFileHeaders.GSTType));
		customerDto.setTaxExempt(Boolean.valueOf(csvRecord.get(CustomerCSVFileHeaders.IsTaxExempt)));
		customerDto.setGreeting(csvRecord.get(CustomerCSVFileHeaders.Greeting));
		customerDto.setCreditStatus(csvRecord.get(CustomerCSVFileHeaders.CreditStatus));
		customerDto.setRating(csvRecord.get(CustomerCSVFileHeaders.Rating));
		boolean isAllowed = Integer.valueOf(csvRecord.get(CustomerCSVFileHeaders.AllowDuplicateGSTIN.name())) == 0
				? false
				: true;
		customerDto.setAllowDuplicateGSTIN(isAllowed);
		customerDto.setCustomerGstIn(csvRecord.get(CustomerCSVFileHeaders.CustomerGSTIN));
		customerDto.setSupplyGstIn(csvRecord.get(CustomerCSVFileHeaders.SupplyGSTIN));
		if (!csvRecord.get(CustomerCSVFileHeaders.PhoneNo).isBlank()
				|| !csvRecord.get(CustomerCSVFileHeaders.PhoneNo).isEmpty())
			customerDto.setPhoneNo(Long.valueOf(csvRecord.get(CustomerCSVFileHeaders.PhoneNo)));
		if (!csvRecord.get(CustomerCSVFileHeaders.MobileNo).isEmpty())
			customerDto.setMobileNo(Long.valueOf(csvRecord.get(CustomerCSVFileHeaders.MobileNo)));
		if (!csvRecord.get(CustomerCSVFileHeaders.FaxNumber).isEmpty())
			customerDto.setFaxNumber(Long.valueOf(csvRecord.get(CustomerCSVFileHeaders.FaxNumber)));
		customerDto.setEmail(csvRecord.get(CustomerCSVFileHeaders.Email));
		customerDto.setWebsite(csvRecord.get(CustomerCSVFileHeaders.Website));
		customerDto.setTanNo(csvRecord.get(CustomerCSVFileHeaders.TANNo));
		customerDto.setPanNo(csvRecord.get(CustomerCSVFileHeaders.PANNo));
		customerDto.setSlNo(Integer.valueOf(csvRecord.get(CustomerCSVFileHeaders.SlNo)));
		return customerDto;
	}

	public boolean validateListOfCustomers(List<CustomerDto> customerDtos)
			throws InterruptedException, ExecutionException {
		boolean isValid = false;
		Map<Integer, Map<String, String>> errorsMap = new HashMap<>();
		Set<CustomerDto> validCustomerDtos = new HashSet<>();
		customerDtos.parallelStream().forEach(customerDto -> {
			try {
				Map<String, String> errors = customerCodeValidator.validateCustomerDto(customerDto).get();
				if (!errors.isEmpty()) {
					errorsMap.put(customerDto.getSlNo(), errors);
				} else {
					if (!validCustomerDtos.contains(customerDto)) {
						validCustomerDtos.add(customerDto);
					} else {
						errors.put(CustomerCSVFileHeaders.Duplicate.name(), "duplicate customer record");
						errorsMap.put(customerDto.getSlNo(), errors);
					}
				}
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		if (errorsMap.isEmpty()) {
			isValid = true;
		} else {
				errorLogService.generateErrorLogExcelFile(errorsMap);
		}
		if (!validCustomerDtos.isEmpty()) {
			saveCustomerDtos(validCustomerDtos);
		}
		return isValid;
	}

	@Async
	public void saveCustomerDtos(Set<CustomerDto> customerDtos) {
		List<CustomerDto> parentCustomerDtos = customerDtos.stream().filter(
				customerDto -> (customerDto.getParentCompany().isBlank() || customerDto.getParentCompany() == null))
				.collect(Collectors.toList());
		customerDtos.removeAll(parentCustomerDtos);
		List<Customer> parentCustomers = parentCustomerDtos.parallelStream()
				.map(customerDto -> convertcustomerDtoToObj(customerDto)).collect(Collectors.toList());
		customerRepository.saveAll(parentCustomers);
		List<Customer> childCustomers = customerDtos.parallelStream().map(customerDto -> convertcustomerDtoToObj(customerDto))
				.collect(Collectors.toList());
		customerRepository.saveAll(childCustomers);
	}

	public Customer convertcustomerDtoToObj(CustomerDto customerDto) {
		Customer customer = new Customer();
		customer.setParentcustomerfk(customerRepository.getParentCustomerId(customerDto.getParentCompany()));
		customer.setPanno(customerDto.getPanNo());
		customer.setSupplygstin(customerDto.getSupplyGstIn());
		customer.setCustomergstin(customerDto.getCustomerGstIn());
		customer.setCustomeralias(customerDto.getCustomerAlias());
		customer.setSupplystatefk(dbConfig.getStatesMap().get(customerDto.getSupplyState().toUpperCase()));
		customer.setCustomercode(customerDto.getCustomerCode());
		customer.setCustomertypefk(dbConfig.getCustomerTypes().get(customerDto.getCustomerType().toUpperCase()));
		customer.setCustomername(customerDto.getCustomerName());
		customer.setDataimportfk((customerDto.getDataImportFk())); //
		customer.setBrandfk(brandRepository.fetchByBrandName(customerDto.getBrand()));
		customer.setIstaxexempt(customerDto.isTaxExempt());
		customer.setAllowduplicategstin(customerDto.isAllowDuplicateGSTIN());
		customer.setPhoneno(Long.valueOf(customerDto.getPhoneNo()));
		customer.setMobileno(customerDto.getMobileNo());
		customer.setFaxnumber(customerDto.getFaxNumber());
		customer.setEmail(customerDto.getEmail());
		customer.setWebsite(customerDto.getWebsite());
		customer.setTanno(customerDto.getTanNo());
		customer.setGreetingfk(dbConfig.getGreetings().get(customerDto.getGreeting().toUpperCase()));
		customer.setAllowduplicategstin(customerDto.isAllowDuplicateGSTIN());
		customer.setIstaxexempt(customerDto.isTaxExempt());
		customer.setGsttypefk(dbConfig.getGstnTypes().get(customerDto.getGstType().toUpperCase()));
		customer.setCreditstatusfk(dbConfig.getCreditStatuses().get(customerDto.getCreditStatus().toUpperCase()));
		customer.setRatingfk(dbConfig.getRatings().get(customerDto.getRating().toUpperCase()));
		return customer;
	}
}
