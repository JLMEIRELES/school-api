package com.example.school.mapper;

import com.example.school.records.exceptions.ExceptionData;
import org.springframework.stereotype.Component;

@Component
public class ExceptionMapper {

    public ExceptionData toExceptionData(RuntimeException exception){
        return new ExceptionData(exception.getMessage());
    }

}
