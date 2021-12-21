package com.sicredi.voting.api.subject;

public final class SubjectRequestDTOStub {

    private SubjectRequestDTOStub() {
    }

    public static SubjectRequestDTO create() {
        return SubjectRequestDTO.builder()
            .code("SUBJECT-CODE")
            .title("TESTING SUBJECT")
            .description("TESTING DESCRIPTION")
            .build();
    }
}
