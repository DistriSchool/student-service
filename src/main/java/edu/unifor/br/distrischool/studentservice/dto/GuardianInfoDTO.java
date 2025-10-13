package edu.unifor.br.distrischool.studentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// GuardianInfo DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianInfoDTO {

    @NotBlank(message = "Nome do responsável é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String name;

    @NotBlank(message = "CPF do responsável é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank(message = "Telefone do responsável é obrigatório")
    @Pattern(regexp = "\\d{10,20}", message = "Telefone inválido")
    private String phone;

    @jakarta.validation.constraints.Email(message = "Email do responsável inválido")
    private String email;

    @NotBlank(message = "Relacionamento é obrigatório")
    @Size(max = 50, message = "Relacionamento deve ter no máximo 50 caracteres")
    private String relationship;

    @Size(max = 100, message = "Ocupação deve ter no máximo 100 caracteres")
    private String occupation;
}
