package com.sicredi.voting.infrastructure.session;

import com.sicredi.voting.core.session.SessionBO;
import com.sicredi.voting.core.session.SessionBOStub;
import com.sicredi.voting.core.session.SessionVoteBO;
import com.sicredi.voting.core.session.SessionVoteBOStub;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionMapperTest {

    @Test
    public void shouldConvertToDocument() {
        final SessionBO sessionBO = SessionBOStub.create();
        final SessionDocument sessionDocument = SessionMapper.toDocument(sessionBO);

        assertAll(
            () -> assertNotNull(sessionDocument),
            () -> assertEquals(sessionBO.getSubjectCode(), sessionDocument.getSubjectCode()),
            () -> assertEquals(sessionBO.getExpirationDate(), sessionDocument.getExpirationDate())
        );
    }

    @Test
    public void shouldConvertToBusinessObject() {
        final SessionDocument sessionDocument = SessionDocumentStub.create();
        final SessionBO sessionBO = SessionMapper.toBusinessObject(sessionDocument);

        assertAll(
            () -> assertNotNull(sessionDocument),
            () -> assertNotNull(sessionDocument.getId()),
            () -> assertNotNull(sessionDocument.getCreationDateTime()),
            () -> assertEquals(sessionDocument.getSubjectCode(), sessionBO.getSubjectCode()),
            () -> assertEquals(sessionDocument.getExpirationDate(), sessionBO.getExpirationDate())
        );
    }

    @Test
    public void shouldConvertToVoteDocument() {
        final SessionVoteBO sessionVoteBO = SessionVoteBOStub.create();
        final SessionDocument.VoteDocument voteDocument = SessionMapper.toDocument(sessionVoteBO);

        assertAll(
            () -> assertNotNull(voteDocument),
            () -> assertEquals(sessionVoteBO.getCpf(), voteDocument.getCpf()),
            () -> assertEquals(sessionVoteBO.getOption(), voteDocument.getOption())
        );
    }
}
