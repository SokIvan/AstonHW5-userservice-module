package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void createUser_ShouldSaveUser() {
        UserRequest request = new UserRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setAge(25);

        UserResponse response = userService.createUser(request);

        assertNotNull(response.getId());
        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(25, response.getAge());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        UserRequest request = new UserRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setAge(25);

        UserResponse savedUser = userService.createUser(request);
        UserResponse foundUser = userService.getUserById(savedUser.getId());

        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals(savedUser.getName(), foundUser.getName());
    }

    @Test
    void updateUser_ShouldUpdateUser() {
        UserRequest createRequest = new UserRequest();
        createRequest.setName("Original Name");
        createRequest.setEmail("original@example.com");
        createRequest.setAge(25);

        UserResponse savedUser = userService.createUser(createRequest);

        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setAge(30);

        UserResponse updatedUser = userService.updateUser(savedUser.getId(), updateRequest);

        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals(30, updatedUser.getAge());
    }

    @Test
    void deleteUser_ShouldRemoveUser() {
        UserRequest request = new UserRequest();
        request.setName("User to Delete");
        request.setEmail("delete@example.com");
        request.setAge(25);

        UserResponse savedUser = userService.createUser(request);

        userService.deleteUser(savedUser.getId());

        assertThrows(IllegalArgumentException.class,
                () -> userService.getUserById(savedUser.getId()));
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldThrowException() {
        UserRequest request1 = new UserRequest();
        request1.setName("User 1");
        request1.setEmail("duplicate@example.com");
        request1.setAge(25);

        UserRequest request2 = new UserRequest();
        request2.setName("User 2");
        request2.setEmail("duplicate@example.com"); // Same email
        request2.setAge(30);

        userService.createUser(request1);

        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(request2));
    }
}