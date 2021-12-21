package com.sicredi.voting.api.subject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class SubjectResponseDTO {

    @Schema(description = "Code")
    private final String code;

    @Schema(description = "Title")
    private final String title;

    @Schema(description = "Description")
    private final String description;
}
