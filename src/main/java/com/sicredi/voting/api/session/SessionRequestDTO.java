package com.sicredi.voting.api.session;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@Validated
public class SessionRequestDTO {

    @NotBlank(message = "Subject code is required.")
    @Schema(required = true, description = "Subject code")
    private final String subjectCode;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Schema(pattern = "yyyy-MM-dd'T'HH:mm", description = "Expiration date")
    private final LocalDateTime expirationDate;
}
