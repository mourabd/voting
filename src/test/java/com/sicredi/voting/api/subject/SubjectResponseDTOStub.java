package com.sicredi.voting.api.subject;

public final class SubjectResponseDTOStub {

    private SubjectResponseDTOStub() {
    }

    public static SubjectResponseDTO create() {
        return SubjectResponseDTO.builder()
            .code("SUBJECT-CODE")
            .title("TESTING SUBJECT")
            .description("TESTING DESCRIPTION")
            .build();
    }
}
