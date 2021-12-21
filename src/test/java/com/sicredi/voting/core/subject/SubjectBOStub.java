package com.sicredi.voting.core.subject;

public final class SubjectBOStub {

    private SubjectBOStub() {
    }

    public static SubjectBO create() {
        return SubjectBO.builder()
            .code("SUBJECT-CODE")
            .title("TESTING SUBJECT")
            .description("TESTING DESCRIPTION")
            .build();
    }

    public static SubjectBO create(String subjectCode) {
        return SubjectBO.builder()
            .code(subjectCode)
            .title("TESTING SUBJECT")
            .description("TESTING DESCRIPTION")
            .build();
    }
}
