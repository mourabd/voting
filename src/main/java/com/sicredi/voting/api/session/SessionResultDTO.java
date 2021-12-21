package com.sicredi.voting.api.session;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionResultDTO {

    @Schema(description = "Voting session")
    private final SessionResponseDTO session;

    @Schema(description = "Result")
    private final ResultDTO result;

    @Builder
    @Getter
    @ToString
    public static class ResultDTO {

        @Schema(description = "Number of YES votes")
        private final Long yes;

        @Schema(description = "Number of NO votes")
        private final Long no;
    }
}
