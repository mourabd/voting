package com.sicredi.voting.api.session;

public final class SessionVoteRequestDTOStub {

    private SessionVoteRequestDTOStub() {
    }

    public static SessionVoteRequestDTO create() {
        return SessionVoteRequestDTO.builder()
            .cpf("01596480223")
            .option(Boolean.TRUE)
            .build();
    }
}
