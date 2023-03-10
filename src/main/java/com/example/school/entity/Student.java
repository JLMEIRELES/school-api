package com.example.school.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends User{
    @Column(unique = true)
    private String registration;

    public Student(String name, String cpf, String email, String password, LocalDate bornDate, UserType userType, String registration) {
        super(name, cpf, email, password, bornDate, userType);
        this.registration = registration;
    }
}
