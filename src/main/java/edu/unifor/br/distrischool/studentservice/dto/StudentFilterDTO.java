package edu.unifor.br.distrischool.studentservice.dto;

import edu.unifor.br.distrischool.studentservice.entity.Student.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentFilterDTO {
    private String name;
    private String registrationNumber;
    private String cpf;
    private StudentStatus status;
    private String city;
    private Boolean hasSpecialNeeds;
}