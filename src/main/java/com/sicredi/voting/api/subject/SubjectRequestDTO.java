package com.sicredi.voting.api.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@Builder
@Getter
@ToString
public class SubjectRequestDTO {

    @Schema(description = "Code")
    private final String code;

    @NotBlank(message = "Title is required.")
    @Schema(required = true, description = "Title")
    private final String title;

    @NotBlank(message = "Description is required.")
    @Schema(required = true, description = "Description")
    private final String description;
}
