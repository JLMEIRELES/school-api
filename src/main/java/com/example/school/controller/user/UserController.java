package com.example.school.controller.user;

import com.example.school.annotation.ProtectedEndpoint;
import com.example.school.entity.Student;
import com.example.school.entity.User;
import com.example.school.entity.UserType;
import com.example.school.mapper.UserMapper;
import com.example.school.records.UserData;
import com.example.school.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/all")
    public ResponseEntity getAllUsers(){
        List<Object> users = userService.getAllUsers().stream()
                .map(user -> {
                    if (user.getUserType() == UserType.STUDENT) {
                        return userMapper.studentToStudentData((Student) user);
                    } else {
                        return userMapper.userToUserData(user);
                    }
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @ProtectedEndpoint(selfUpdate = true, allowedUserTypes = {UserType.ADM})
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody @Valid UserData data){
        User user = userService.getById(id);
        return ResponseEntity.ok(userService.updateUser(user, data));
    }

}
