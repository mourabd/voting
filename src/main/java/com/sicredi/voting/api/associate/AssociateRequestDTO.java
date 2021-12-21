package com.sicredi.voting.api.associate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Validated
@Builder
@Getter
@ToString
public class AssociateRequestDTO {

    @NotBlank(message = "CPF is required.")
    @Pattern(regexp = "^-?\\d{11}$", message = "Attribute CPF must have 11 digits.")
    @Schema(required = true, description = "CPF")
    private final String cpf;

    @NotBlank(message = "First name is required.")
    @Schema(required = true, description = "First name")
    private final String firstName;

    @NotBlank(message = "Last name is required.")
    @Schema(required = true, description = "Last name")
    private final String lastName;
}
