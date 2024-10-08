package com.app.service.customer.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.service.customer.exceptions.InvalidFileTypeException;
import com.app.service.customer.services.CustomerService;
/**
 * API to handle customer csv file uploads
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	CustomerService customerService;

	/**
	 * 
	 * @param file
	 * @return
	 * @throws InvalidFileTypeException if file formats other than csv are uploaded 
	 * @throws IOException if any exceptions occur while reading uploaded csv file
	 * @throws Exception handles other exceptions
	 */
	@PostMapping("/upload")
	public ResponseEntity<Object> uploadCustomerInfo(@RequestParam("file") MultipartFile file)
			throws InvalidFileTypeException, IOException, Exception {
		if (file.getContentType().equals("text/csv")) {
			customerService.uploadCustomerInfo(file.getInputStream());
		} else {
			throw new InvalidFileTypeException("invalid file type");
		}
		return ResponseEntity.ok().build();
	}
}
