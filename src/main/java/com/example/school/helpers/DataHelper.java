package com.example.school.helpers;

import org.springframework.cglib.core.Local;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DataHelper {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String toString(LocalDate date){
        return dateFormat.format(date);
    }

    public static LocalDate toDate(String date) {
        return LocalDate.parse(date, dateFormat);
    }
}
