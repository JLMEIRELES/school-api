package com.example.school.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataHelper {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static String toString(Date date){
        return dateFormat.format(date);
    }
}
