package com.example.school.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String toString(LocalDate date){
        return dateFormat.format(date);
    }

    public static LocalDate toDate(String date) {
        return LocalDate.parse(date, dateFormat);
    }
}
