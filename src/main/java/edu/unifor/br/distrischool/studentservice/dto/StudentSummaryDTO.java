package edu.unifor.br.distrischool.studentservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.unifor.br.distrischool.studentservice.entity.Student;
import edu.unifor.br.distrischool.studentservice.entity.Student.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSummaryDTO {

    private Long id;
    private String registrationNumber;
    private String fullName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private Integer age;
    private StudentStatus status;
    private String photoUrl;

    public static StudentSummaryDTO from(Student student) {
        return StudentSummaryDTO.builder()
                .id(student.getId())
                .registrationNumber(student.getRegistrationNumber())
                .fullName(student.getFullName())
                .dateOfBirth(student.getDateOfBirth())
                .age(calculateAge(student.getDateOfBirth()))
                .build();
    }

    private static Integer calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return null;
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }
}