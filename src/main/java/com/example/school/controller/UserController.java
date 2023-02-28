package com.example.school.controller;

import com.example.school.mapper.UserMapper;
import com.example.school.records.UserData;
import com.example.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/all")
    public ResponseEntity<List<UserData>> getAllUsers(){
        List<UserData> users = new ArrayList<>();
        userService.getAllUsers().forEach(user -> users.add(userMapper.userToUserData(user)));
        return ResponseEntity.ok(users);
    }
}
