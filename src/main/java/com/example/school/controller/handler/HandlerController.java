package com.example.school.controller.handler;

import com.example.school.mapper.ExceptionMapper;
import com.example.school.records.exceptions.HandlerData;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerController {

    @Autowired
    private ExceptionMapper exceptionMapper;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity error404Treatment() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity error400Treatment(MethodArgumentNotValidException ex) {
        var err = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(err.stream().map(HandlerData::new).toList());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity error403Treatment(RuntimeException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionMapper.toExceptionData(exception));
    }

}
