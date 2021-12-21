package com.sicredi.voting.api.associate;

public final class AssociateResponseDTOStub {

    private AssociateResponseDTOStub() {
    }

    public static AssociateResponseDTO create() {
        return AssociateResponseDTO.builder()
            .cpf("01596480223")
            .firstName("John")
            .lastName("Doe")
            .build();
    }
}
