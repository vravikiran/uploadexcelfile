package com.app.service.customer.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolConfig {
	 @Bean(name = "validateCustFieldsByNamesExecutor")
	    public Executor validateCustFieldsByNamesExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(4);
	        executor.setMaxPoolSize(4);
	        executor.setThreadNamePrefix("validateCustFieldsByNamesExecutor-");
	        executor.initialize();
	        return executor;
	    }

	    @Bean(name = "validateCustFieldsExecutor")
	    public Executor custFieldsValidatorByNamesExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(4);
	        executor.setMaxPoolSize(4);
	        executor.setThreadNamePrefix("validateCustFieldsExecutor-");
	        executor.initialize();
	        return executor;
	    }
}
