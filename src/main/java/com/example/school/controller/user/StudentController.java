package com.example.school.controller.user;

import com.example.school.annotation.ProtectedEndpoint;
import com.example.school.entity.Student;
import com.example.school.entity.UserType;
import com.example.school.mapper.UserMapper;
import com.example.school.records.StudentData;
import com.example.school.records.UserData;
import com.example.school.records.UserRequestData;
import com.example.school.service.StudentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/create-student")
    @Transactional
    @ProtectedEndpoint(allowedUserTypes = {UserType.ADM})
    public ResponseEntity createStudent(@RequestBody @Valid UserRequestData requestData, UriComponentsBuilder uriBuilder) {
        Student student = studentService.createStudent(requestData);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(student.getId()).toUri();
        UserData response = userMapper.userToUserData(student);
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/registration/{registration}")
    public ResponseEntity getStudentByRegistration(@PathVariable String registration){
        Student student = studentService.getStudentByRegistration(registration);
        return ResponseEntity.ok(userMapper.studentToStudentData(student));
    }
}
