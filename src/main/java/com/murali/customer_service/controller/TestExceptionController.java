package com.murali.customer_service.controller;

import com.murali.customer_service.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/*
## 9) Notes & troubleshooting

- Ensure there are no mixed logging implementations on the classpath (you fixed versions already).
    Use `mvn dependency:tree -Dincludes=log4j` to verify.
- The MDC key name in the filter (`requestId`) must match `%X{requestId}` in pattern.
- If you use `JsonLayout` the file will be JSON lines — excellent for ELK/Fluentd.
- Clear `.m2` if you see `NoSuchMethodError` referencing mismatched log4j jars;
        then `mvn clean package`.
*/
@RestController
@RequestMapping("/api/test")
public class TestExceptionController {

    @GetMapping("/notfound")
    public String notFound() {
        throw new ResourceNotFoundException("test-resource not found");
    }

    @GetMapping("/illegal")
    public String illegal() {
        throw new IllegalArgumentException("bad input passed");
    }
}