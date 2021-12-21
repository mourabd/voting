package com.sicredi.voting.infrastructure.subject;

import com.sicredi.voting.core.subject.SubjectBO;
import com.sicredi.voting.core.subject.SubjectBOStub;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubjectMapperTest {

    @Test
    public void shouldConvertToDocument() {
        final SubjectBO subjectBO = SubjectBOStub.create();
        final SubjectDocument subjectDocument = SubjectMapper.toDocument(subjectBO);

        assertAll(
            () -> assertNotNull(subjectDocument),
            () -> assertEquals(subjectBO.getCode(), subjectDocument.getCode()),
            () -> assertEquals(subjectBO.getTitle(), subjectDocument.getTitle()),
            () -> assertEquals(subjectBO.getDescription(), subjectDocument.getDescription())
        );
    }

    @Test
    public void shouldConvertToBusinessObject() {
        final SubjectDocument subjectDocument = SubjectDocumentStub.create();
        final SubjectBO subjectBO = SubjectMapper.toBusinessObject(subjectDocument);

        assertAll(
            () -> assertNotNull(subjectDocument),
            () -> assertNotNull(subjectDocument.getId()),
            () -> assertNotNull(subjectDocument.getCreationDateTime()),
            () -> assertEquals(subjectDocument.getCode(), subjectBO.getCode()),
            () -> assertEquals(subjectDocument.getTitle(), subjectBO.getTitle()),
            () -> assertEquals(subjectDocument.getDescription(), subjectBO.getDescription())
        );
    }
}
