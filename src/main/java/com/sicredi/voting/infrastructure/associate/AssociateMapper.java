package com.sicredi.voting.infrastructure.associate;

import com.sicredi.voting.core.associate.AssociateBO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssociateMapper {

    public static AssociateDocument toDocument(AssociateBO associateBO) {
        return AssociateDocument.builder()
            .cpf(associateBO.getCpf())
            .firstName(associateBO.getFirstName())
            .lastName(associateBO.getLastName())
            .build();
    }

    public static AssociateBO toBusinessObject(AssociateDocument associateDocument) {
        return AssociateBO.builder()
            .cpf(associateDocument.getCpf())
            .firstName(associateDocument.getFirstName())
            .lastName(associateDocument.getLastName())
            .build();
    }
}
