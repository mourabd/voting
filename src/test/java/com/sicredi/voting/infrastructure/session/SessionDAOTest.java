package com.sicredi.voting.infrastructure.session;

import com.sicredi.voting.core.session.SessionBO;
import com.sicredi.voting.core.session.SessionBOStub;
import com.sicredi.voting.core.session.SessionVoteBO;
import com.sicredi.voting.core.session.SessionVoteBOStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionDAOTest {

    private static final String CODE = "CODE";
    private static final String CPF = "CPF";

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionDAO sessionDAO;

    @Test
    public void shouldReturnTrueWhenSubjectCodeIsRegistered() {

        // when
        when(sessionRepository.existsBySubjectCode(CODE)).thenReturn(Mono.just(Boolean.TRUE));

        // then
        assertTrue(sessionDAO.existsBySubjectCode(CODE).block());
    }

    @Test
    public void shouldReturnFalseWhenSubjectCodeIsNotRegistered() {

        // when
        when(sessionRepository.existsBySubjectCode(CODE)).thenReturn(Mono.just(Boolean.FALSE));

        // then
        assertFalse(sessionDAO.existsBySubjectCode(CODE).block());
    }

    @Test
    public void shouldReturnTrueWhenAssociateAlreadyVoted() {

        // when
        when(sessionRepository.existsBySubjectCodeAndVotesCpf(CODE, CPF)).thenReturn(Mono.just(Boolean.TRUE));

        // then
        assertTrue(sessionDAO.existsAssociateVote(CODE, CPF).block());
    }

    @Test
    public void shouldReturnFalseWhenAssociateNotVoted() {

        // when
        when(sessionRepository.existsBySubjectCodeAndVotesCpf(CODE, CPF)).thenReturn(Mono.just(Boolean.FALSE));

        // then
        assertFalse(sessionDAO.existsAssociateVote(CODE, CPF).block());
    }

    @Test
    public void shouldReturnSessionBOWhenSubjectCodeIsRegistered() {

        // given
        final SessionDocument sessionDocument = SessionDocumentStub.create();
        final SessionBO expectedResponse = SessionBOStub.create();

        // when
        when(sessionRepository.findBySubjectCode(CODE)).thenReturn(Mono.just(sessionDocument));

        // then
        final SessionBO response = sessionDAO.findBySubjectCode(CODE).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getStatus(), response.getStatus()),
            () -> assertEquals(expectedResponse.getSubjectCode(), response.getSubjectCode()),
            () -> assertEquals(expectedResponse.getExpirationDate(), response.getExpirationDate())
        );
    }

    @Test
    public void shouldReturnAllRegisteredSessionBOs() {

        // given
        final int FIRST_ELEMENT = 0;
        final SessionDocument sessionDocument = SessionDocumentStub.create();
        final List<SessionBO> expectedResponse = Collections.singletonList(SessionBOStub.create());

        // when
        when(sessionRepository.findAll(Sort.by(Sort.Direction.DESC, "expirationDate"))).thenReturn(Flux.just(sessionDocument));

        // then
        final List<SessionBO> response = sessionDAO.findAll().collectList().block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.size(), response.size()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getStatus(), response.get(FIRST_ELEMENT).getStatus()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getSubjectCode(), response.get(FIRST_ELEMENT).getSubjectCode()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getExpirationDate(), response.get(FIRST_ELEMENT).getExpirationDate())
        );
    }

    @Test
    public void shouldRegisterSession() {

        // given
        final SessionDocument sessionDocument = SessionDocumentStub.create();
        final SessionBO expectedResponse = SessionBOStub.create();

        // when
        when(sessionRepository.save(any())).thenReturn(Mono.just(sessionDocument));

        // then
        final SessionBO response = sessionDAO.save(Mono.just(expectedResponse)).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getStatus(), response.getStatus()),
            () -> assertEquals(expectedResponse.getSubjectCode(), response.getSubjectCode()),
            () -> assertEquals(expectedResponse.getExpirationDate(), response.getExpirationDate())
        );
    }

    @Test
    public void shouldUpdateSessionVote() {

        // given
        final SessionVoteBO sessionVoteBO = SessionVoteBOStub.create();
        final SessionDocument sessionDocument = SessionDocumentStub.create();
        sessionDocument.withVotes(null);
        final SessionBO expectedResponse = SessionBOStub.create();

        // when
        when(sessionRepository.findBySubjectCode(expectedResponse.getSubjectCode())).thenReturn(Mono.just(sessionDocument));
        when(sessionRepository.save(any())).thenReturn(Mono.just(sessionDocument));

        // then
        final SessionBO response = sessionDAO.update(Mono.just(sessionVoteBO)).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getStatus(), response.getStatus()),
            () -> assertEquals(expectedResponse.getSubjectCode(), response.getSubjectCode()),
            () -> assertEquals(expectedResponse.getExpirationDate(), response.getExpirationDate())
        );
    }
}
