package com.example.userservice.service;

import com.example.userservice.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserEvent(UserEvent userEvent) {
        try {
            kafkaTemplate.send("user-events", userEvent);
            log.info("Sent user event: {} for user: {}",
                    userEvent.getEventType(), userEvent.getUserEmail());
        } catch (Exception e) {
            log.error("Failed to send user event to Kafka", e);
        }
    }
}