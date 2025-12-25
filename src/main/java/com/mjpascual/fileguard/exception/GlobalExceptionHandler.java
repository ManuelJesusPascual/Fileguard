package com.mjpascual.fileguard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


/**
 * Global exception handler for the FileGuard API.
 *
 * This class intercepts exceptions thrown by REST controllers and transforms them
 * into standardized JSON error responses. It ensures that all unhandled exceptions
 * are caught and returned with appropriate HTTP status codes and error messages.
 *
 */
@RestControllerAdvice(basePackages = "com.mjpascual.fileguard.controller")
public class GlobalExceptionHandler {

    /**
     * This method serves as a catch-all handler for any exception that is not
     * specifically handled elsewhere. It returns a standardized error response
     * containing the exception message.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing a map with the error message and HTTP 400 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleAll(Exception ex) {
        Map<String,String> resp = new HashMap<>();
        resp.put("error","Unexpected Error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }
}
