package com.sicredi.voting.core.associate;

public final class AssociateBOStub {

    private AssociateBOStub() {
    }

    public static AssociateBO create() {
        return AssociateBO.builder()
            .cpf("01596480223")
            .firstName("John")
            .lastName("Doe")
            .build();
    }
}
