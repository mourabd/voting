package com.sicredi.voting.infrastructure.associate;

import com.sicredi.voting.core.associate.AssociateBO;
import com.sicredi.voting.core.associate.AssociateBOStub;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssociateMapperTest {

    @Test
    public void shouldConvertToDocument() {
        final AssociateBO associateBO = AssociateBOStub.create();
        final AssociateDocument associateDocument = AssociateMapper.toDocument(associateBO);

        assertAll(
            () -> assertNotNull(associateDocument),
            () -> assertEquals(associateBO.getCpf(), associateDocument.getCpf()),
            () -> assertEquals(associateBO.getFirstName(), associateDocument.getFirstName()),
            () -> assertEquals(associateBO.getLastName(), associateDocument.getLastName())
        );
    }

    @Test
    public void shouldConvertToBusinessObject() {
        final AssociateDocument associateDocument = AssociateDocumentStub.create();
        final AssociateBO associateBO = AssociateMapper.toBusinessObject(associateDocument);

        assertAll(
            () -> assertNotNull(associateDocument),
            () -> assertNotNull(associateDocument.getId()),
            () -> assertNotNull(associateDocument.getCreationDateTime()),
            () -> assertEquals(associateDocument.getCpf(), associateBO.getCpf()),
            () -> assertEquals(associateDocument.getFirstName(), associateBO.getFirstName()),
            () -> assertEquals(associateDocument.getLastName(), associateBO.getLastName())
        );
    }
}
