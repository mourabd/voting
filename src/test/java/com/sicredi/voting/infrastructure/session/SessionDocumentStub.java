package com.sicredi.voting.infrastructure.session;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

public final class SessionDocumentStub {

    private SessionDocumentStub() {
    }

    public static SessionDocument create() {
        return SessionDocument.builder()
            .id("SESSION_ID")
            .subjectCode("SUBJECT-CODE")
            .expirationDate(LocalDateTime.of(2022, Month.JANUARY, 10, 15, 0, 0))
            .creationDateTime(LocalDateTime.of(2022, Month.JANUARY, 10, 15, 0, 0))
            .votes(Collections.singletonList(VoteDocumentStub.create()))
            .build();
    }

    public static final class VoteDocumentStub {

        private VoteDocumentStub() {
        }

        public static SessionDocument.VoteDocument create() {
            return SessionDocument.VoteDocument.builder()
                .cpf("01596480223")
                .option(Boolean.TRUE)
                .build();
        }

        public static SessionDocument.VoteDocument create(boolean option) {
            return SessionDocument.VoteDocument.builder()
                .cpf("01596480223")
                .option(option)
                .build();
        }
    }
}
