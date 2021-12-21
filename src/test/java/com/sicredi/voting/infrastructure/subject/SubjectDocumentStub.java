package com.sicredi.voting.infrastructure.subject;

import java.time.LocalDateTime;
import java.time.Month;

public final class SubjectDocumentStub {

    private SubjectDocumentStub() {
    }

    public static SubjectDocument create() {
        return SubjectDocument.builder()
            .id("SUBJECT_ID")
            .code("SUBJECT-CODE")
            .title("TESTING SUBJECT")
            .description("TESTING DESCRIPTION")
            .creationDateTime(LocalDateTime.of(2022, Month.JANUARY, 10, 15, 0, 0))
            .build();
    }
}
