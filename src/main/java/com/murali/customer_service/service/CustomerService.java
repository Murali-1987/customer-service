package com.murali.customer_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @CircuitBreaker(name="customerServiceCB", fallbackMethod = "customerFallBackMethod")
    public List<String> getCustomers() {
        // Replace with real downstream call (DB/HTTP). Here simulate success.
        return List.of("Murali","Dhanu","Rahini","Shriyansh");
    }

    @CircuitBreaker(name = "customerCB", fallbackMethod = "customerFallback")
    public List<String> getCustomers(boolean fail) {
        if (fail) {
            throw new RuntimeException("forced-failure-for-testing");
        }
        return List.of("Murali","Dhanu","Rahini","Shriyansh");
    }

    public List<String> customerFallback(boolean fail, Throwable ex) {
        // fallback signature: same args + Throwable
        return List.of("service-unavailable");
    }

    public List<String> customerFallBackMethod(Throwable t) {
        System.out.println("Fallback executed due to: " + t.getMessage());

        return List.of(
                "Fallback Customer", "fallback@example.com"
        );
    }


}
