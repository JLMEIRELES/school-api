package com.example.school.helpers;

import java.time.LocalDate;
import java.util.Random;

public class StringHelper {
    public static String generateRegistrationForStudent(){
        String year = String.valueOf(LocalDate.now().getYear());
        String random = String.format("%03d", new Random().nextInt(1000));
        return year + random;
    }
}
