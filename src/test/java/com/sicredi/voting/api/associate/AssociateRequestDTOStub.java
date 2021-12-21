package com.sicredi.voting.api.associate;

public final class AssociateRequestDTOStub {

    private AssociateRequestDTOStub() {
    }

    public static AssociateRequestDTO create() {
        return AssociateRequestDTO.builder()
            .cpf("01596480223")
            .firstName("John")
            .lastName("Doe")
            .build();
    }
}
