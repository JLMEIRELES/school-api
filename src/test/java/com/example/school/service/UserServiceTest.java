package com.example.school.service;

import com.example.school.entity.Student;
import com.example.school.entity.User;
import com.example.school.entity.UserType;
import com.example.school.mapper.UserMapper;
import com.example.school.records.StudentData;
import com.example.school.records.UserData;
import com.example.school.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        Student student = new Student();
        student.setUserType(UserType.STUDENT);
        User user = new User();
        user.setUserType(UserType.ADM);
        users.add(student);
        users.add(user);

        List<Record> expectedUsers = new ArrayList<>();
        expectedUsers.add(new StudentData("Test", "28/02/2001", "email@email.com", "STUDENT", "00000", "12345678910"));
        expectedUsers.add(new UserData("Test", "28/02/2001", "email@email.com", "ADM",  "12345678910"));

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.studentToStudentData(any(Student.class))).thenReturn((StudentData) expectedUsers.get(0));
        when(userMapper.userToUserData(any(User.class))).thenReturn((UserData) expectedUsers.get(1));

        List<Record> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers.size(), actualUsers.size());
        for (int i = 0; i < expectedUsers.size(); i++) {
            assertEquals(expectedUsers.get(i), actualUsers.get(i));
        }
    }

    @Test
    public void testGetById(){
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(userService.getById(1L), user);
        assertNull(userService.getById(2L));
    }

    @Test
    public void testFindByEmail(){
        User user = new User();
        when(userRepository.findByEmail("email@email.com")).thenReturn(user);

        assertEquals(userService.findByEmail("email@email.com"), user);
        assertNull(userService.findByEmail("test"));
    }

    @Test
    public void testUpdateUser() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("email@email.com");

        UserData userData = new UserData("Test", "28/02/2001", "email@email.com", null, "12345678910");


        when(userRepository.save(any())).thenReturn(user);

        User updatedUser = userService.updateUser(user, userData);

        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(userData.name(), updatedUser.getName());
        assertEquals(userData.email(), updatedUser.getEmail());
    }

    @Test
    public void testShouldNotChangeUserType(){
        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("email@email.com");

        UserData userData = new UserData("Test", "28/02/2001", "email@email.com", "ADM", "12345678910");


        when(userRepository.save(any())).thenReturn(user);

        Throwable exception = assertThrows(RuntimeException.class, () -> userService.updateUser(user, userData));
        assertEquals(exception.getMessage(), "User cannot have his role changed");
    }

    @Test
    public void testShouldNotSaveAnInvalidDate(){
        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("email@email.com");

        UserData userData = new UserData("Test", "31/02/2001", "email@email.com", null, "12345678910");


        when(userRepository.save(any())).thenReturn(user);

        Throwable exception = assertThrows(RuntimeException.class, () -> userService.updateUser(user, userData));
        assertEquals(exception.getMessage(), "invalid date: " + userData.bornDate());
    }
}