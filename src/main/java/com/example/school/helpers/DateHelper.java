package com.example.school.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DateHelper {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static String toString(LocalDate date){
        return dateFormat.format(date);
    }

    public static LocalDate toDate(String date) {
        try{
            return LocalDate.parse(date, dateFormat);
        } catch (DateTimeParseException e){
            throw new RuntimeException("invalid date: " + date);
        }
    }
}
