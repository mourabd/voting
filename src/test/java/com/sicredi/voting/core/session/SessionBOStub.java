package com.sicredi.voting.core.session;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

public final class SessionBOStub {

    private SessionBOStub() {
    }

    public static SessionBO create() {
        return SessionBO.builder()
            .status(SessionStatusEnum.OPEN)
            .subjectCode("SUBJECT-CODE")
            .expirationDate(LocalDateTime.of(2022, Month.JANUARY, 10, 15, 0, 0))
            .votes(Collections.singletonList(VoteBOtStub.create()))
            .build();
    }

    public static SessionBO create(LocalDateTime expirationDate) {
        return SessionBO.builder()
            .status(SessionStatusEnum.OPEN)
            .subjectCode("SUBJECT-CODE")
            .expirationDate(expirationDate)
            .build();
    }

    public static final class VoteBOtStub {

        private VoteBOtStub() {
        }

        public static SessionBO.VoteBO create() {
            return SessionBO.VoteBO.builder()
                .cpf("01596480223")
                .option(Boolean.TRUE)
                .build();
        }
    }
}
