package edu.unifor.br.distrischool.studentservice.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Contact DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    @Pattern(regexp = "\\d{0,20}", message = "Telefone inválido")
    private String phone;

    @Pattern(regexp = "\\d{0,20}", message = "Celular inválido")
    private String mobile;

    @jakarta.validation.constraints.Email(message = "Email alternativo inválido")
    private String alternativeEmail;
}
