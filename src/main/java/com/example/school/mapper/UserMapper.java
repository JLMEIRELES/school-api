package com.example.school.mapper;

import com.example.school.entity.Student;
import com.example.school.entity.User;
import com.example.school.helpers.DateHelper;
import com.example.school.records.StudentData;
import com.example.school.records.UserData;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserData userToUserData(User user){
        return new UserData(user.getName(), DateHelper.toString(user.getBornDate()), user.getEmail(), user.getUserType().toString(), user.getCpf());
    }

    public StudentData studentToStudentData(Student student){
        return new StudentData(student.getName(), DateHelper.toString(student.getBornDate()), student.getEmail(), student.getUserType().toString(), student.getRegistration(), student.getCpf());
    }
}
