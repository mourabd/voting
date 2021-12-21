package com.sicredi.voting.core.session;

import com.sicredi.voting.api.session.SessionResponseDTO;
import com.sicredi.voting.api.session.SessionResponseDTOStub;
import com.sicredi.voting.config.exception.NotFoundException;
import com.sicredi.voting.infrastructure.session.SessionDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionServiceImplTest {

    private static final String SUBJECT_CODE = "SUBJECT_CODE";

    @Mock
    private SessionDAO sessionDAO;

    @Mock
    private SessionValidatorService sessionValidatorService;

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Test
    public void shouldFindSessionBySubjectCode() {

        // given
        final SessionBO expectedResponse = SessionBOStub.create();
        final SessionBO sessionBO = SessionBOStub.create();

        // when
        when(sessionDAO.findBySubjectCode(SUBJECT_CODE)).thenReturn(Mono.just(sessionBO));

        // then
        final SessionBO response = sessionService.findBySubjectCode(SUBJECT_CODE).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getStatus(), response.getStatus()),
            () -> assertEquals(expectedResponse.getSubjectCode(), response.getSubjectCode()),
            () -> assertEquals(expectedResponse.getExpirationDate(), response.getExpirationDate())
        );
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenSessionIsNotFound() {

        // when
        when(sessionDAO.findBySubjectCode(SUBJECT_CODE)).thenReturn(Mono.error(new NotFoundException("Session not found")));

        // then
        StepVerifier
            .create(sessionService.findBySubjectCode(SUBJECT_CODE))
            .expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().equals("Session not found"))
            .verify();
    }

    @Test
    public void shouldFindAllRegisteredSessions() {

        // given
        final int FIRST_ELEMENT = 0;
        final SessionBO sessionBO = SessionBOStub.create();
        final List<SessionBO> expectedResponse = Collections.singletonList(sessionBO);

        // when
        when(sessionDAO.findAll()).thenReturn(Flux.just(sessionBO));

        // then
        final List<SessionBO> response = sessionService.findAll().collectList().block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.size(), response.size()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getStatus(), response.get(FIRST_ELEMENT).getStatus()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getSubjectCode(), response.get(FIRST_ELEMENT).getSubjectCode()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getExpirationDate(), response.get(FIRST_ELEMENT).getExpirationDate())
        );
    }

    @Test
    public void shouldSaveSession() {

        // given
        final Mono<SessionBO> sessionBOMono = Mono.just(SessionBOStub.create());
        final SessionResponseDTO expectedResponse = SessionResponseDTOStub.create();

        // when
        when(sessionValidatorService.validateSession(any())).thenReturn(sessionBOMono);
        when(sessionDAO.save(any())).thenReturn(sessionBOMono);

        // then
        final SessionBO response = sessionService.save(sessionBOMono).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getStatus(), response.getStatus()),
            () -> assertEquals(expectedResponse.getSubjectCode(), response.getSubjectCode()),
            () -> assertEquals(expectedResponse.getExpirationDate(), response.getExpirationDate())
        );
    }

    @Test
    public void shouldAddSessionVote() {

        // given
        final SessionVoteBO sessionVoteBO = SessionVoteBOStub.create();
        final Mono<SessionVoteBO> sessionVoteBOMono = Mono.just(sessionVoteBO);
        final SessionBO sessionBO = SessionBOStub.create();
        final SessionResponseDTO expectedResponse = SessionResponseDTOStub.create();

        // when
        when(sessionValidatorService.validateSessionVote(sessionVoteBOMono)).thenReturn(sessionVoteBOMono);
        when(sessionDAO.update(any())).thenReturn(Mono.just(sessionBO));

        // then
        final SessionBO response = sessionService.updateVote(sessionVoteBOMono).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getStatus(), response.getStatus()),
            () -> assertEquals(expectedResponse.getSubjectCode(), response.getSubjectCode()),
            () -> assertEquals(expectedResponse.getExpirationDate(), response.getExpirationDate())
        );
    }

    @Test
    public void shouldFindResultBySubjectCode() {

        // given
        final SessionBO sessionBO = SessionBOStub.create();
        final SessionResultBO expectedResponse = SessionResultBOStub.create();

        // when
        when(sessionDAO.findBySubjectCode(SUBJECT_CODE)).thenReturn(Mono.just(sessionBO));

        // then
        final SessionResultBO response = sessionService.findResultBySubjectCode(SUBJECT_CODE).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertNotNull(response.getSession()),
            () -> assertEquals(expectedResponse.getSession().getStatus(), response.getSession().getStatus()),
            () -> assertEquals(expectedResponse.getSession().getSubjectCode(), response.getSession().getSubjectCode()),
            () -> assertEquals(expectedResponse.getSession().getExpirationDate(), response.getSession().getExpirationDate()),
            () -> assertNotNull(response.getResult()),
            () -> assertEquals(expectedResponse.getResult().getYes(), response.getResult().getYes()),
            () -> assertEquals(expectedResponse.getResult().getNo(), response.getResult().getNo())
        );
    }

    @Test
    public void shouldThrowSessionNotFoundExceptionWhenSessionIsNotFound() {

        // when
        when(sessionDAO.findBySubjectCode(SUBJECT_CODE)).thenReturn(Mono.error(new NotFoundException("Session not found")));

        // then
        StepVerifier
            .create(sessionService.findResultBySubjectCode(SUBJECT_CODE))
            .expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().equals("Session not found"))
            .verify();
    }
}
