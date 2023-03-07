package com.example.school.service;

import com.example.school.entity.Student;
import com.example.school.entity.UserType;
import com.example.school.helpers.DateHelper;
import com.example.school.records.UserRequestData;
import com.example.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Student createStudent(UserRequestData requestData) {
        if (!requestData.password().equals(requestData.passwordConfirmation())){
            throw new RuntimeException("Password and password confirmation must match");
        }
        Student student = new Student(requestData.name(), requestData.cpf(), requestData.email(),
                passwordEncoder.encode(requestData.password()), DateHelper.toDate(requestData.bornDate()),
                UserType.STUDENT, generateRegistrationForStudent());
        return studentRepository.save(student);
    }

    public Student getStudentByRegistration(String registration){
        if (registration.length() != 7){
            throw new RuntimeException("Invalid registration");
        }
        Student student = studentRepository.getByRegistration(registration);
        if (student == null){
            throw new RuntimeException("No student found for this registration");
        }
        return student;
    }

    private String generateRegistrationForStudent(){
        String year = String.valueOf(LocalDate.now().getYear());
        String random = String.format("%03d", new Random().nextInt(1000));
        String registration = year + random;
        return studentRepository.getByRegistration(registration) == null ? registration : generateRegistrationForStudent();
    }
}
