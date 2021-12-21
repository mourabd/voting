package com.sicredi.voting.api.session;

import com.sicredi.voting.core.session.SessionBO;
import com.sicredi.voting.core.session.SessionBOStub;
import com.sicredi.voting.core.session.SessionResultBO;
import com.sicredi.voting.core.session.SessionResultBOStub;
import com.sicredi.voting.core.session.SessionService;
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
public class SessionControllerTest {

    private static final String SUBJECT_CODE = "SUBJECT_CODE";

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SessionController sessionController;

    @Test
    public void shouldFindSessionBySubjectCode() {

        // given
        final SessionResponseDTO expectedResponse = SessionResponseDTOStub.create();
        final SessionBO sessionBO = SessionBOStub.create();

        // when
        when(sessionService.findBySubjectCode(SUBJECT_CODE)).thenReturn(Mono.just(sessionBO));

        // then
        final SessionResponseDTO response = sessionController.findBySubjectCode(SUBJECT_CODE).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getStatus(), response.getStatus()),
            () -> assertEquals(expectedResponse.getSubjectCode(), response.getSubjectCode()),
            () -> assertEquals(expectedResponse.getExpirationDate(), response.getExpirationDate())
        );
    }

    @Test
    public void shouldFindAllRegisteredSessions() {

        // given
        final int FIRST_ELEMENT = 0;
        final List<SessionResponseDTO> expectedResponse = Collections.singletonList(SessionResponseDTOStub.create());
        final SessionBO sessionBO = SessionBOStub.create();

        // when
        when(sessionService.findAll()).thenReturn(Flux.just(sessionBO));

        // then
        final List<SessionResponseDTO> response = sessionController.findAll().collectList().block();

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
        final SessionRequestDTO sessionRequestDTO = SessionRequestDTOStub.create();
        final SessionBO sessionBO = SessionBOStub.create();
        final SessionResponseDTO expectedResponse = SessionResponseDTOStub.create();

        // when
        when(sessionService.save(any())).thenReturn(Mono.just(sessionBO));

        // then
        final SessionResponseDTO response = sessionController.save(Mono.just(sessionRequestDTO)).block();

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
        final SessionVoteRequestDTO sessionRequestDTO = SessionVoteRequestDTOStub.create();
        final SessionBO sessionBO = SessionBOStub.create();

        // when
        when(sessionService.updateVote(any())).thenReturn(Mono.just(sessionBO));

        // then
        StepVerifier.create(sessionController.addVote(SUBJECT_CODE, Mono.just(sessionRequestDTO))).verifyComplete();
    }

    @Test
    public void shouldFindSessionResultBySubjectCode() {

        // given
        final SessionResultDTO expectedResponse = SessionResultDTOStub.create();
        final SessionResultBO sessionResultBO = SessionResultBOStub.create();

        // when
        when(sessionService.findResultBySubjectCode(SUBJECT_CODE)).thenReturn(Mono.just(sessionResultBO));

        // then
        final SessionResultDTO response = sessionController.findResult(SUBJECT_CODE).block();

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
}
