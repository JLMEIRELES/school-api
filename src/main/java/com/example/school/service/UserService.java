package com.example.school.service;

import com.example.school.entity.Student;
import com.example.school.entity.User;
import com.example.school.entity.UserType;
import com.example.school.helpers.DataHelper;
import com.example.school.records.StudentData;
import com.example.school.records.UserData;
import com.example.school.records.UserRequestData;
import com.example.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email){
        return (User) userRepository.findByEmail(email);
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Student createStudent(UserRequestData requestData) {
        if (!requestData.password().equals(requestData.passwordConfirmation())){
            throw new RuntimeException("Password and password confirmation must match");
        }
        Student student = new Student(requestData.name(), requestData.cpf(), requestData.email(),
                requestData.password(), DataHelper.toDate(requestData.bornDate()), UserType.valueOf(requestData.userType()), )
    }
}
