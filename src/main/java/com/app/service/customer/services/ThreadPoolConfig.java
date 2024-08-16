package com.app.service.customer.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
/**
 * Contains thread pool configuration to run parallel tasks while validating customer fields retrieved from excel sheet
 */
@Configuration
public class ThreadPoolConfig {
	/**
	 * creates thread pool for customerFieldsByNameService instances
	 * @return
	 */
	 @Bean(name = "validateCustFieldsByNamesExecutor")
	    public Executor validateCustFieldsByNamesExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(4);
	        executor.setMaxPoolSize(4);
	        executor.setThreadNamePrefix("validateCustFieldsByNamesExecutor-");
	        executor.initialize();
	        return executor;
	    }

	 	/**
	 	 * creates thread pool of CustomerFieldsValidator service instances
	 	 * @return
	 	 */
	    @Bean(name = "validateCustFieldsExecutor")
	    public Executor custFieldsValidatorExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(4);
	        executor.setMaxPoolSize(4);
	        executor.setThreadNamePrefix("validateCustFieldsExecutor-");
	        executor.initialize();
	        return executor;
	    }
}
