package com.sicredi.voting.infrastructure.subject;

import com.sicredi.voting.core.subject.SubjectBO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SubjectMapper {

    public static SubjectDocument toDocument(SubjectBO subjectBO) {
        return SubjectDocument.builder()
            .code(subjectBO.getCode())
            .title(subjectBO.getTitle())
            .description(subjectBO.getDescription())
            .build();
    }

    public static SubjectBO toBusinessObject(SubjectDocument subjectDocument) {
        return SubjectBO.builder()
            .code(subjectDocument.getCode())
            .title(subjectDocument.getTitle())
            .description(subjectDocument.getDescription())
            .build();
    }
}
