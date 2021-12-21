package com.sicredi.voting.api.session;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sicredi.voting.core.session.SessionStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class SessionResponseDTO {

    @Schema(description = "Status")
    private final SessionStatusEnum status;

    @Schema(description = "Subject code")
    private final String subjectCode;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Schema(pattern = "yyyy-MM-dd'T'HH:mm", description = "Expiration date")
    private final LocalDateTime expirationDate;
}
