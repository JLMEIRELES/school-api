package com.example.school.service;

import com.example.school.entity.Student;
import com.example.school.entity.UserType;
import com.example.school.helpers.DataHelper;
import com.example.school.helpers.StringHelper;
import com.example.school.records.UserRequestData;
import com.example.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                passwordEncoder.encode(requestData.password()), DataHelper.toDate(requestData.bornDate()),
                UserType.STUDENT, StringHelper.generateRegistrationForStudent());
        return studentRepository.save(student);
    }
}
