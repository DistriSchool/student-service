package edu.unifor.br.distrischool.teacherservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.unifor.br.distrischool.teacherservice.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {

    private Long id;
    private Long userId;
    private String registrationNumber;
    private String fullName;
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private Integer age;

    private String cpf;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public static StudentResponseDTO from(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .userId(student.getUserId())
                .registrationNumber(student.getRegistrationNumber())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .dateOfBirth(student.getDateOfBirth())
                .age(calculateAge(student.getDateOfBirth()))
                .cpf(maskCpf(student.getCpf()))
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }

    private static Integer calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return null;
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    private static String maskCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.substring(0, 3) + ".***.***-" + cpf.substring(9);
    }
}