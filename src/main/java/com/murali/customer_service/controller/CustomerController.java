package com.murali.customer_service.controller;

import com.murali.customer_service.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    //CONSTRUCTOR BEAN INJECTION
    private final CustomerService service;
    public CustomerController(CustomerService service) { this.service = service; }

    //@Autowired
    //private final CustomerService service;

    @GetMapping("/api/customers")
    public ResponseEntity<List<String>> customers() {
        return ResponseEntity.ok(service.getCustomers());
    }

    // Accept query param ?fail=true to simulate failure
    @GetMapping("/api/customers_cb")
    public List<String> customers(@RequestParam(name="fail", required=false, defaultValue="false") boolean fail) {
        return service.getCustomers(fail);
    }

    @GetMapping("/api/customers/health")
    public String health() { return "OK"; }
}
