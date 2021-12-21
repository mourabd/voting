package com.sicredi.voting.api.session;

import com.sicredi.voting.core.session.SessionStatusEnum;

import java.time.LocalDateTime;
import java.time.Month;

public final class SessionResponseDTOStub {

    private SessionResponseDTOStub() {
    }

    public static SessionResponseDTO create() {
        return SessionResponseDTO.builder()
            .status(SessionStatusEnum.OPEN)
            .subjectCode("SUBJECT-CODE")
            .expirationDate(LocalDateTime.of(2022, Month.JANUARY, 10, 15, 0, 0))
            .build();
    }
}
