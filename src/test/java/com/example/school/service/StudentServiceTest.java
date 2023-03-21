package com.example.school.service;

import com.example.school.entity.Student;
import com.example.school.mapper.UserMapper;
import com.example.school.records.UserRequestData;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-test.properties")
public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateStudentWithDifferentPasswords(){
        UserRequestData requestData = new UserRequestData("test", "28/02/1998","email@email.com",  "70801167167", "password", "passwordConfirmation");

        Throwable exception = assertThrows(RuntimeException.class, () -> studentService.createStudent(requestData));
        assertEquals(exception.getMessage(), "Password and password confirmation must match");
    }

    @Test
    public void testShouldCreateNewStudent(){
        UserRequestData requestData = new UserRequestData("test", "28/02/1998","email@email.com",  "70801167167", "password", "password");
        String name = "test";
        Student student = new Student();
        student.setName(name);
        when(studentRepository.save(any())).thenReturn(student);
        assertEquals(studentService.createStudent(requestData), student);
    }

    @Test
    public void testShouldNotCallDataBase(){
        Throwable exception = assertThrows(RuntimeException.class, () -> studentService.getStudentByRegistration("12"));
        assertEquals(exception.getMessage(), "Invalid registration");

        when(studentRepository.getByRegistration(anyString())).thenReturn(null);

        Throwable secondException = assertThrows(RuntimeException.class, () -> studentService.getStudentByRegistration("1234567"));
        assertEquals(secondException.getMessage(), "No student found for this registration");
    }
}
