package com.example.school.controller.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerController {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity error404Treatment() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity error400Treatment(MethodArgumentNotValidException ex) {
        var err = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(err.stream().map(HandlerData::new).toList());
    }

    private record HandlerData(String field, String message) {
        public HandlerData(FieldError e) {
            this(e.getField(), e.getDefaultMessage());
        }
    }
}
