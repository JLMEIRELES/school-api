package com.example.school.service;

import com.example.school.entity.Student;
import com.example.school.entity.User;
import com.example.school.entity.UserType;
import com.example.school.helpers.DataHelper;
import com.example.school.helpers.StringHelper;
import com.example.school.records.UserRequestData;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

}
