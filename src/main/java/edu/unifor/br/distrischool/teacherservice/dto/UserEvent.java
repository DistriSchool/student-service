package edu.unifor.br.distrischool.teacherservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private String eventType;
    private Long userId;
    private String email;
    private String name;
    private String role; // e.g. "STUDENT"
    private String password; // plaintext temporary password to be created by auth-service
    private String timestamp;

}
