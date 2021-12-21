package com.sicredi.voting.api.associate;

import com.sicredi.voting.core.associate.AssociateBO;
import com.sicredi.voting.core.associate.AssociateBOStub;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssociateApiMapperTest {

    @Test
    public void shouldConvertDataTransferObjectToBusinessObject() {
        final AssociateBO expectedResponse = AssociateBOStub.create();
        final AssociateBO response = AssociateApiMapper.toBusinessObject(AssociateRequestDTOStub.create());

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCpf(), response.getCpf()),
            () -> assertEquals(expectedResponse.getFirstName(), response.getFirstName()),
            () -> assertEquals(expectedResponse.getLastName(), response.getLastName())
        );
    }

    @Test
    public void shouldConvertBusinessObjectToDataTransferObject() {
        final AssociateResponseDTO expectedResponse = AssociateResponseDTOStub.create();
        final AssociateResponseDTO response = AssociateApiMapper.toDataTransferObject(AssociateBOStub.create());

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCpf(), response.getCpf()),
            () -> assertEquals(expectedResponse.getFirstName(), response.getFirstName()),
            () -> assertEquals(expectedResponse.getLastName(), response.getLastName())
        );
    }
}
