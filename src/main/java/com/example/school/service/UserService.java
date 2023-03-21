package com.example.school.service;

import com.example.school.entity.Student;
import com.example.school.entity.User;
import com.example.school.entity.UserType;
import com.example.school.helpers.DateHelper;
import com.example.school.mapper.UserMapper;
import com.example.school.records.UserData;
import com.example.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public User findByEmail(String email){
        return (User) userRepository.findByEmail(email);
    }
    public List<Record> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> user.getUserType() == UserType.STUDENT || user.getUserType() == UserType.ADM)
                .map(user -> {
                    if (user.getUserType() == UserType.STUDENT) {
                        return userMapper.studentToStudentData((Student) user);
                    } else {
                        return userMapper.userToUserData(user);
                    }
                })
                .collect(Collectors.toList());
    }

    public User getById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(User user, UserData data){
        Field[] fields = UserData.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(data);
                if (value != null) {
                    Field userField = User.class.getDeclaredField(field.getName());
                    if (field.getName().equals("bornDate")){
                        value = DateHelper.toDate((String) value);
                    }
                    if (field.getName().equals("userType")){
                        if (!UserType.valueOf((String) value).equals(user.getUserType())){
                            throw new RuntimeException("User cannot have his role changed");
                        }
                        value = UserType.valueOf((String) value);
                    }
                    userField.setAccessible(true);
                    userField.set(user, value);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return userRepository.save(user);
    }
}
