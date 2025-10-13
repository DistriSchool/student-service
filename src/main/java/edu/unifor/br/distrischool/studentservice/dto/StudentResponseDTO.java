package edu.unifor.br.distrischool.studentservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.unifor.br.distrischool.studentservice.entity.Student;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private Integer age; // Calculado

    private String cpf;
    private String rg;
    private String rgIssuer;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rgIssueDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    private Long createdBy;
    private Long updatedBy;

    public static StudentResponseDTO from(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .registrationNumber(student.getRegistrationNumber())
                .fullName(student.getFullName())
                .dateOfBirth(student.getDateOfBirth())
                .age(calculateAge(student.getDateOfBirth()))
                .cpf(maskCpf(student.getCpf()))
                .rg(student.getRg())
                .rgIssuer(student.getRgIssuer())
                .rgIssueDate(student.getRgIssueDate())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .createdBy(student.getCreatedBy())
                .updatedBy(student.getUpdatedBy())
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