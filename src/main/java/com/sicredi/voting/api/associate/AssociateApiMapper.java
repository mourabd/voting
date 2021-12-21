package com.sicredi.voting.api.associate;

import com.sicredi.voting.core.associate.AssociateBO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssociateApiMapper {

    public static AssociateBO toBusinessObject(AssociateRequestDTO associateRequestDTO) {
        return AssociateBO.builder()
            .cpf(associateRequestDTO.getCpf())
            .firstName(associateRequestDTO.getFirstName())
            .lastName(associateRequestDTO.getLastName())
            .build();
    }

    public static AssociateResponseDTO toDataTransferObject(AssociateBO associateBO) {
        return AssociateResponseDTO.builder()
            .cpf(associateBO.getCpf())
            .firstName(associateBO.getFirstName())
            .lastName(associateBO.getLastName())
            .build();
    }
}
