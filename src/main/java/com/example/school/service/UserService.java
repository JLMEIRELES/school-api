package com.example.school.service;

import com.example.school.entity.User;
import com.example.school.mapper.UserMapper;
import com.example.school.records.UserData;
import com.example.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

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
