package com.murali.customer_service.exception;


import com.murali.customer_service.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.time.Instant;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);


    private String currentRequestId() {
// if you use slf4j MDC or another request id mechanism
        String id = MDC.get("requestId");
        return id == null ? "n/a" : id;
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        String requestId = currentRequestId();
        ErrorResponse body = new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                ex.getMessage(), request.getRequestURI(), requestId);


// Log full stack with requestId in MDC already set by filter
        logger.error("Unhandled exception for requestId={}: {}", requestId, ex.getMessage(), ex);


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        String requestId = currentRequestId();
        ErrorResponse body = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request",
                ex.getMessage(), request.getRequestURI(), requestId);
        logger.warn("Bad request requestId={}: {}", requestId, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String requestId = currentRequestId();
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ErrorResponse body = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Validation Failed",
                message, request.getRequestURI(), requestId);
        logger.info("Validation failed requestId={}: {}", requestId, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    //Global Exception Handling And Logging (controller Advice)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        String requestId = currentRequestId();
        ErrorResponse body = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), "Not Found",
                ex.getMessage(), request.getRequestURI(), requestId);
        logger.info("NotFound requestId={}: {}", requestId, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}