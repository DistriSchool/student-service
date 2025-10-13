package edu.unifor.br.distrischool.studentservice.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    @Size(max = 200, message = "Rua deve ter no máximo 200 caracteres")
    private String street;

    @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
    private String number;

    @Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
    private String complement;

    @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
    private String neighborhood;

    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String city;

    @Pattern(regexp = "[A-Z]{2}", message = "Estado deve ter 2 letras maiúsculas (ex: CE)")
    private String state;

    @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos")
    private String zipCode;

    @Size(max = 50, message = "País deve ter no máximo 50 caracteres")
    private String country;
}

