package com.murali.customer_service;

import com.murali.customer_service.logging.RequestIdFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	// Register the filter as a bean (if not already):
	// in a @Configuration class or main application
	@Bean
	public FilterRegistrationBean<RequestIdFilter> requestIdFilterRegistration() {
		FilterRegistrationBean<RequestIdFilter> reg = new FilterRegistrationBean<>();
		reg.setFilter(new RequestIdFilter());
		reg.setOrder(1);
		reg.addUrlPatterns("/*");
		return reg;
	}
}
