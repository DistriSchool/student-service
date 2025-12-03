package edu.unifor.br.distrischool.teacherservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateDTO {

    @Size(min = 3, max = 150, message = "Nome deve ter entre 3 e 150 caracteres")
    private String fullName;

    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @Min(value = 1, message = "Semestre deve ser no mínimo 1")
    @Max(value = 12, message = "Semestre deve ser no máximo 12")
    private Integer semester;

    @Pattern(regexp = "\\d{0,20}", message = "RG deve conter apenas números")
    private String rg;

    @Size(max = 50, message = "Órgão emissor deve ter no máximo 50 caracteres")
    private String rgIssuer;

    private LocalDate rgIssueDate;
}
