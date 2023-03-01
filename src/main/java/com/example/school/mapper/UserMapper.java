package com.example.school.mapper;

import com.example.school.entity.User;
import com.example.school.helpers.DataHelper;
import com.example.school.records.UserData;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserData userToUserData(User user){
        return new UserData(user.getName(), DataHelper.toString(user.getBornDate()), user.getEmail(), user.getUserType().toString());
    }

}
