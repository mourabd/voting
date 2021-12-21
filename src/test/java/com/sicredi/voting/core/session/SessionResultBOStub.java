package com.sicredi.voting.core.session;

public final class SessionResultBOStub {

    private SessionResultBOStub() {
    }

    public static SessionResultBO create() {
        return SessionResultBO.builder()
            .result(ResultBOStub.create())
            .session(SessionBOStub.create())
            .build();
    }

    private static final class ResultBOStub {
        public static SessionResultBO.ResultBO create() {
            return SessionResultBO.ResultBO.builder()
                .yes(1L)
                .no(0L)
                .build();
        }
    }
}
