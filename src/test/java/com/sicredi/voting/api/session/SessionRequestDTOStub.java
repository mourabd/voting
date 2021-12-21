package com.sicredi.voting.api.session;

import java.time.LocalDateTime;
import java.time.Month;

public final class SessionRequestDTOStub {

    private SessionRequestDTOStub() {
    }

    public static SessionRequestDTO create() {
        return SessionRequestDTO.builder()
            .subjectCode("SUBJECT-CODE")
            .expirationDate(LocalDateTime.of(2022, Month.JANUARY, 10, 15, 0, 0))
            .build();
    }
}
