package com.sicredi.voting.core.session;

public final class SessionVoteBOStub {

    private SessionVoteBOStub() {
    }

    public static SessionVoteBO create() {
        return SessionVoteBO.builder()
            .subjectCode("SUBJECT-CODE")
            .cpf("01596480223")
            .option(Boolean.TRUE)
            .build();
    }
}
