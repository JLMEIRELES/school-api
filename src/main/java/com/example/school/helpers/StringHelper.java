package com.example.school.helpers;

import java.time.LocalDate;

public class StringHelper {
    public static String generateRegistrationForStudent(){
        return String.valueOf(LocalDate.now().getYear()).concat(String.valueOf( Math.random() * (10000)));
    }
}
