package com.example.userservice.service;

import com.example.userservice.dto.UserEvent;
import com.example.userservice.dto.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class UserServiceKafkaIntegrationTest {

    @Autowired
    private UserService userService;

    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void createUser_ShouldSendUserCreatedEvent() {
        UserRequest request = new UserRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setAge(25);

        userService.createUser(request);

        // Проверяем, что сообщение было отправлено в Kafka
        verify(kafkaTemplate, timeout(5000)).send(
                eq("user-events"),
                any(UserEvent.class)
        );
    }

    @Test
    void deleteUser_ShouldSendUserDeletedEvent() {
        UserRequest request = new UserRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setAge(25);

        var savedUser = userService.createUser(request);
        userService.deleteUser(savedUser.getId());

        // Проверяем, что сообщение было отправлено в Kafka
        verify(kafkaTemplate, timeout(5000).times(2)).send(
                eq("user-events"),
                any(UserEvent.class)
        );
    }
}