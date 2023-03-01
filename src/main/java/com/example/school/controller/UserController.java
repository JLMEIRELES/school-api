package com.example.school.controller;

import com.example.school.entity.Student;
import com.example.school.mapper.UserMapper;
import com.example.school.records.UserData;
import com.example.school.records.UserRequestData;
import com.example.school.service.StudentService;
import com.example.school.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/all")
    public ResponseEntity<List<UserData>> getAllUsers(){
        List<UserData> users = new ArrayList<>();
        userService.getAllUsers().forEach(user -> users.add(userMapper.userToUserData(user)));
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create-student")
    @Transactional
    public ResponseEntity createStudent(@RequestBody @Valid UserRequestData requestData, UriComponentsBuilder uriBuilder){
        Student student = studentService.createStudent(requestData);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(student.getId()).toUri();

        return ResponseEntity.created(uri).body(student);
    }
}
