package com.sicredi.voting.api.subject;

import com.sicredi.voting.core.subject.SubjectBOStub;
import com.sicredi.voting.core.subject.SubjectBO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubjectApiMapperTest {

    @Test
    public void shouldConvertDataTransferObjectToBusinessObject() {
        final SubjectBO expectedResponse = SubjectBOStub.create();
        final SubjectBO response = SubjectApiMapper.toBusinessObject(SubjectRequestDTOStub.create());

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCode(), response.getCode()),
            () -> assertEquals(expectedResponse.getTitle(), response.getTitle()),
            () -> assertEquals(expectedResponse.getDescription(), response.getDescription())
        );
    }

    @Test
    public void shouldConvertBusinessObjectToDataTransferObject() {
        final SubjectResponseDTO expectedResponse = SubjectResponseDTOStub.create();
        final SubjectResponseDTO response = SubjectApiMapper.toDataTransferObject(SubjectBOStub.create());

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCode(), response.getCode()),
            () -> assertEquals(expectedResponse.getTitle(), response.getTitle()),
            () -> assertEquals(expectedResponse.getDescription(), response.getDescription())
        );
    }
}
