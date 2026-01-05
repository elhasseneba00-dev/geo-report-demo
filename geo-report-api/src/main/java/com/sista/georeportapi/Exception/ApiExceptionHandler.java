package com.sista.georeportapi.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentExceptionHandler(IllegalArgumentException exc) {
        return ResponseEntity.badRequest().body(Map.of("error", exc.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<?> validation(MethodArgumentNotValidException exc) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "validation_error");
        body.put("details",  exc.getBindingResult().getFieldErrors().stream()
                .map(fe -> {
                    assert fe.getDefaultMessage() != null;
                    return Map.of("field", fe.getField(), "message", fe.getDefaultMessage());
                })
                .toList());
        return ResponseEntity.badRequest().body(body);
    }
}
