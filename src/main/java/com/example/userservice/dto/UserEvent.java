package com.example.userservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserEvent {
    private String eventType; // "USER_CREATED" или "USER_DELETED"
    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDateTime eventTime;

    public UserEvent(String eventType, Long userId, String userName, String userEmail) {
        this.eventType = eventType;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.eventTime = LocalDateTime.now();
    }
}