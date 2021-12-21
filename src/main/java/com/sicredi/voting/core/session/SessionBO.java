package com.sicredi.voting.core.session;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@ToString
@With
public class SessionBO {

    private final SessionStatusEnum status;
    private final String subjectCode;
    private final LocalDateTime expirationDate;
    private final List<VoteBO> votes;

    @Builder
    @Getter
    @ToString
    public static class VoteBO {
        private final String cpf;
        private final Boolean option;
    }
}
