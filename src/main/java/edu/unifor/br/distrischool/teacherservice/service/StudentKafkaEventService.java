package edu.unifor.br.distrischool.teacherservice.service;

import edu.unifor.br.distrischool.teacherservice.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentKafkaEventService {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void publishUserCreateEvent(UserEvent event) {
        try {
            kafkaTemplate.send("user.create", event);
            log.info("Published user.create event for email={}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to publish user.create event: {}", e.getMessage(), e);
            throw e;
        }
    }
}

