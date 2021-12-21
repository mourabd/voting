package com.sicredi.voting.api.session;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@ToString
@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionVoteRequestDTO {

    @NotBlank(message = "CPF is required.")
    @Schema(required = true, description = "CPF")
    private final String cpf;

    @NotNull(message = "Option is required.")
    @Schema(required = true, description = "Option")
    private final Boolean option;
}
