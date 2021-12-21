package com.sicredi.voting.api.session;

import com.sicredi.voting.core.session.SessionBO;
import com.sicredi.voting.core.session.SessionBOStub;
import com.sicredi.voting.core.session.SessionResultBOStub;
import com.sicredi.voting.core.session.SessionVoteBO;
import com.sicredi.voting.core.session.SessionVoteBOStub;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionApiMapperTest {

    @Test
    public void shouldConvertDataTransferObjectToBusinessObject() {
        final SessionBO expectedResponse = SessionBOStub.create();
        final SessionBO response = SessionApiMapper.toBusinessObject(SessionRequestDTOStub.create());

        assertAll(
            () -> assertNotNull(response),
            () -> assertNull(response.getStatus()),
            () -> assertEquals(expectedResponse.getSubjectCode(), response.getSubjectCode()),
            () -> assertEquals(expectedResponse.getExpirationDate(), response.getExpirationDate())
        );
    }

    @Test
    public void shouldConvertSessionVoteDataTransferObjectToBusinessObject() {
        final SessionVoteBO expectedResponse = SessionVoteBOStub.create();
        final SessionVoteBO response = SessionApiMapper.toBusinessObject("SUBJECT-CODE", SessionVoteRequestDTOStub.create());

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCpf(), response.getCpf()),
            () -> assertEquals(expectedResponse.getSubjectCode(), response.getSubjectCode()),
            () -> assertEquals(expectedResponse.getOption(), response.getOption())
        );
    }

    @Test
    public void shouldConvertBusinessObjectToDataTransferObject() {
        final SessionResponseDTO expectedResponse = SessionResponseDTOStub.create();
        final SessionResponseDTO response = SessionApiMapper.toDataTransferObject(SessionBOStub.create());

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getStatus(), response.getStatus()),
            () -> assertEquals(expectedResponse.getSubjectCode(), response.getSubjectCode()),
            () -> assertEquals(expectedResponse.getExpirationDate(), response.getExpirationDate())
        );
    }

    @Test
    public void shouldConvertSessionResultBusinessObjectToDataTransferObject() {
        final SessionResultDTO expectedResponse = SessionResultDTOStub.create();
        final SessionResultDTO response = SessionApiMapper.toDataTransferObject(SessionResultBOStub.create());

        assertAll(
            () -> assertNotNull(response),
            () -> assertNotNull(response.getResult()),
            () -> assertEquals(expectedResponse.getResult().getYes(), response.getResult().getYes()),
            () -> assertEquals(expectedResponse.getResult().getNo(), response.getResult().getNo()),
            () -> assertNotNull(response.getSession()),
            () -> assertEquals(expectedResponse.getSession().getStatus(), response.getSession().getStatus()),
            () -> assertEquals(expectedResponse.getSession().getSubjectCode(), response.getSession().getSubjectCode()),
            () -> assertEquals(expectedResponse.getSession().getExpirationDate(), response.getSession().getExpirationDate())
        );
    }
}
