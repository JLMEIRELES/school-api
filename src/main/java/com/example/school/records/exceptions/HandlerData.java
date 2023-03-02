package com.example.school.records.exceptions;

import org.springframework.validation.FieldError;

public record HandlerData(String field, String message) {
    public HandlerData(FieldError e) {
        this(e.getField(), e.getDefaultMessage());
    }
}