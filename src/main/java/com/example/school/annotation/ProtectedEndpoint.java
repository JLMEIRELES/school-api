package com.example.school.annotation;

import com.example.school.entity.UserType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProtectedEndpoint {
    UserType[] allowedUserTypes() default {};

    boolean selfUpdate() default false;
}