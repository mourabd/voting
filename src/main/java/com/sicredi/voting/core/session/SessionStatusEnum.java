package com.sicredi.voting.core.session;

import java.time.LocalDateTime;

public enum SessionStatusEnum {

    OPEN, CLOSED;

    public static SessionStatusEnum fromExpirationDate(LocalDateTime expirationDate) {
        return expirationDate.isAfter(LocalDateTime.now())
            ? SessionStatusEnum.OPEN
            : SessionStatusEnum.CLOSED;
    }
}
