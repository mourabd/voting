package com.sicredi.voting.api.session;

public final class SessionResultDTOStub {

    private SessionResultDTOStub() {

    }

    public static SessionResultDTO create() {
        return SessionResultDTO.builder()
            .result(ResultDTOStub.create())
            .session(SessionResponseDTOStub.create())
            .build();
    }

    private static final class ResultDTOStub {
        public static SessionResultDTO.ResultDTO create() {
            return SessionResultDTO.ResultDTO.builder()
                .yes(1L)
                .no(0L)
                .build();
        }
    }
}
