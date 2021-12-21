package com.sicredi.voting.infrastructure.associate;

import java.time.LocalDateTime;
import java.time.Month;

public final class AssociateDocumentStub {

    private AssociateDocumentStub() {
    }

    public static AssociateDocument create() {
        return AssociateDocument.builder()
            .id("ASSOCIATE_ID")
            .cpf("01596480223")
            .firstName("John")
            .lastName("Doe")
            .creationDateTime(LocalDateTime.of(2022, Month.JANUARY, 10, 15, 0, 0))
            .build();
    }
}
