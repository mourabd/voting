package com.sicredi.voting.api.associate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class AssociateResponseDTO {

    @Schema(description = "CPF")
    private final String cpf;

    @Schema(description = "First name")
    private final String firstName;

    @Schema(description = "Last name")
    private final String lastName;
}
