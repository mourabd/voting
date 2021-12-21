package com.sicredi.voting.core.session;

import com.sicredi.voting.config.exception.BadRequestException;
import com.sicredi.voting.config.exception.NotFoundException;
import com.sicredi.voting.infrastructure.associate.AssociateDAO;
import com.sicredi.voting.infrastructure.session.SessionDAO;
import com.sicredi.voting.infrastructure.subject.SubjectDAO;
import com.sicredi.voting.infrastructure.user.UserDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionValidatorServiceImplTest {

    @Mock
    private SessionDAO sessionDAO;

    @Mock
    private AssociateDAO associateDAO;

    @Mock
    private SubjectDAO subjectDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private SessionValidatorServiceImpl sessionValidatorService;

    @Test
    public void shouldThrowNotFoundExceptionWhenSubjectNotExists() {

        // given
        final SessionBO sessionBO = SessionBOStub.create(LocalDateTime.now().minus(Duration.ofMinutes(1L)));

        // when
        when(subjectDAO.existsByCode(sessionBO.getSubjectCode())).thenReturn(Mono.just(Boolean.FALSE));

        // then
        StepVerifier
            .create(sessionValidatorService.validateSession(Mono.just(sessionBO)))
            .expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().equals("Subject not found"))
            .verify();
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenExpirationDateIsNotValid() {

        // given
        final SessionBO sessionBO = SessionBOStub.create(LocalDateTime.now().minus(Duration.ofMinutes(1L)));

        // when
        when(subjectDAO.existsByCode(sessionBO.getSubjectCode())).thenReturn(Mono.just(Boolean.TRUE));

        // then
        StepVerifier
            .create(sessionValidatorService.validateSession(Mono.just(sessionBO)))
            .expectErrorMatches(throwable -> throwable instanceof BadRequestException && throwable.getMessage().equals("Invalid expiration date"))
            .verify();
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenSessionIsAlreadyRegistered() {

        // given
        final SessionBO sessionBO = SessionBOStub.create();

        // when
        when(subjectDAO.existsByCode(sessionBO.getSubjectCode())).thenReturn(Mono.just(Boolean.TRUE));
        when(sessionDAO.existsBySubjectCode(sessionBO.getSubjectCode())).thenReturn(Mono.just(Boolean.TRUE));

        // then
        StepVerifier
            .create(sessionValidatorService.validateSession(Mono.just(sessionBO)))
            .expectErrorMatches(throwable -> throwable instanceof BadRequestException && throwable.getMessage().equals("Session already registered"))
            .verify();
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenSessionNotExists() {

        // given
        final SessionVoteBO sessionVoteBO = SessionVoteBOStub.create();

        // when
        when(sessionDAO.existsBySubjectCode(sessionVoteBO.getSubjectCode())).thenReturn(Mono.just(Boolean.FALSE));

        // then
        StepVerifier
            .create(sessionValidatorService.validateSessionVote(Mono.just(sessionVoteBO)))
            .expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().equals("Session not found"))
            .verify();
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenAssociateNotExists() {

        // given
        final SessionVoteBO sessionVoteBO = SessionVoteBOStub.create();

        // when
        when(sessionDAO.existsBySubjectCode(sessionVoteBO.getSubjectCode())).thenReturn(Mono.just(Boolean.TRUE));
        when(associateDAO.existsByCpf(sessionVoteBO.getCpf())).thenReturn(Mono.just(Boolean.FALSE));

        // then
        StepVerifier
            .create(sessionValidatorService.validateSessionVote(Mono.just(sessionVoteBO)))
            .expectErrorMatches(throwable -> throwable instanceof BadRequestException && throwable.getMessage().equals("Invalid associate"))
            .verify();
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenSessionIsNotActive() {

        // given
        final SessionVoteBO sessionVoteBO = SessionVoteBOStub.create();
        final SessionBO sessionBO = SessionBOStub.create(LocalDateTime.now().minus(Duration.ofMinutes(30)));

        // when
        when(sessionDAO.existsBySubjectCode(sessionVoteBO.getSubjectCode())).thenReturn(Mono.just(Boolean.TRUE));
        when(associateDAO.existsByCpf(sessionVoteBO.getCpf())).thenReturn(Mono.just(Boolean.TRUE));
        when(sessionDAO.findBySubjectCode(sessionVoteBO.getSubjectCode())).thenReturn(Mono.just(sessionBO));

        // then
        StepVerifier
            .create(sessionValidatorService.validateSessionVote(Mono.just(sessionVoteBO)))
            .expectErrorMatches(throwable ->
                throwable instanceof BadRequestException && throwable.getMessage().equals("Session already closed for voting"))
            .verify();
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenAssociateAlreadyVoted() {

        // given
        final SessionVoteBO sessionVoteBO = SessionVoteBOStub.create();
        final SessionBO sessionBO = SessionBOStub.create();

        // when
        when(sessionDAO.existsBySubjectCode(sessionVoteBO.getSubjectCode())).thenReturn(Mono.just(Boolean.TRUE));
        when(associateDAO.existsByCpf(sessionVoteBO.getCpf())).thenReturn(Mono.just(Boolean.TRUE));
        when(sessionDAO.findBySubjectCode(sessionVoteBO.getSubjectCode())).thenReturn(Mono.just(sessionBO));
        when(sessionDAO.existsAssociateVote(sessionVoteBO.getSubjectCode(), sessionVoteBO.getCpf())).thenReturn(Mono.just(Boolean.TRUE));

        // then
        StepVerifier
            .create(sessionValidatorService.validateSessionVote(Mono.just(sessionVoteBO)))
            .expectErrorMatches(throwable -> throwable instanceof BadRequestException && throwable.getMessage().equals("Associate already voted"))
            .verify();
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenAssociateIsUnableToVote() {

        // given
        final SessionVoteBO sessionVoteBO = SessionVoteBOStub.create();
        final SessionBO sessionBO = SessionBOStub.create();

        // when
        when(sessionDAO.existsBySubjectCode(sessionVoteBO.getSubjectCode())).thenReturn(Mono.just(Boolean.TRUE));
        when(associateDAO.existsByCpf(sessionVoteBO.getCpf())).thenReturn(Mono.just(Boolean.TRUE));
        when(sessionDAO.findBySubjectCode(sessionVoteBO.getSubjectCode())).thenReturn(Mono.just(sessionBO));
        when(sessionDAO.existsAssociateVote(sessionVoteBO.getSubjectCode(), sessionVoteBO.getCpf())).thenReturn(Mono.just(Boolean.FALSE));
        when(userDAO.isAbleToVote(sessionVoteBO.getCpf())).thenReturn(Mono.just(Boolean.FALSE));

        // then
        StepVerifier
            .create(sessionValidatorService.validateSessionVote(Mono.just(sessionVoteBO)))
            .expectErrorMatches(throwable -> throwable instanceof BadRequestException && throwable.getMessage().equals("Associate unable to vote"))
            .verify();
    }
}
