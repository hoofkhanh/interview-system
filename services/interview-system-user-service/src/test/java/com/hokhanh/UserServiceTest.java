package com.hokhanh;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hokhanh.user.mapper.UserMapper;
import com.hokhanh.user.model.User;
import com.hokhanh.user.repository.UserRepository;
import com.hokhanh.user.request.CreateUserInput;
import com.hokhanh.user.response.BaseUserPayload;
import com.hokhanh.user.response.CreateUserPayload;
import com.hokhanh.user.response.UserByEmailPayload;
import com.hokhanh.user.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@InjectMocks
    private UserService userService;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    private CreateUserInput input;
    private User userWithoutId;
    private User userWithId;
    private BaseUserPayload baseUserPayload;
    private CreateUserPayload createUserPayload;
    private UserByEmailPayload userByEmailPayload;

    @BeforeEach
    void setUp() {
        setUpBase();
        setUpCreateUser();
        setUpUserByEmail();
    }

 // This is the base setup for the other setups below
    private void setUpBase() {
        input = new CreateUserInput(
            "khanh@gmail.com",
            "123",
            true,
            "Khanh",
            "Hồ",
            "Hồ Tuấn Khanh",
            LocalDate.parse("2003-08-18")
        );

        userWithoutId = new User(
            null,
            input.email(),
            input.phoneNumber(),
            input.gender(),
            input.firstName(),
            input.lastName(),
            input.fullName(),
            input.dateOfBirth()
        );

        userWithId = new User(
            UUID.fromString("00000000-0000-0000-0000-000000000123"), 
            input.email(),
            input.phoneNumber(),
            input.gender(),
            input.firstName(),
            input.lastName(),
            input.fullName(),
            input.dateOfBirth()
        );
        
        baseUserPayload = new BaseUserPayload(
            userWithId.getId(),
            userWithId.getEmail(),
            userWithId.getPhoneNumber(),
            userWithId.getGender(),
            userWithId.getFirstName(),
            userWithId.getLastName(),
            userWithId.getFullName(),
            userWithId.getDateOfBirth()
        );
    }

    private void setUpCreateUser() {
        createUserPayload = new CreateUserPayload(baseUserPayload);
    }

    private void setUpUserByEmail() {
        userByEmailPayload = new UserByEmailPayload(baseUserPayload);
    }
    
    // This test case is intended for the createUser method in the service class
    @Test
    void createUser_shouldReturnPayload() {
        // Mock mapper và repository
        when(mapper.toUser(input)).thenReturn(userWithoutId);
        when(repository.save(userWithoutId)).thenReturn(userWithId);
        when(mapper.toBaseUserPayload(userWithId)).thenReturn(baseUserPayload);
        
        
        // Gọi service
        CreateUserPayload result = userService.createUser(input);

        // Assert
        assertEquals(createUserPayload, result);

        // Verify repository và mapper được gọi đúng
        verify(mapper).toUser(input);
        verify(repository).save(userWithoutId);
        verify(mapper).toBaseUserPayload(userWithId);
    }
    
    // This test case is intended for the userByEmail method in the service class
    @Test
    void userByEmail_shouldReturnPayload() {
        String email = "khanh@gmail.com";

        // Mock repository và mapper
        when(repository.findByEmail(email)).thenReturn(userWithId);
        when(mapper.toBaseUserPayload(userWithId)).thenReturn(baseUserPayload);

        // Gọi service
        UserByEmailPayload result = userService.userByEmail(email);

        // Assert
        assertEquals(userByEmailPayload, result);

        // Verify repository và mapper được gọi đúng
        verify(repository).findByEmail(email);
        verify(mapper).toBaseUserPayload(userWithId);
    }


}
