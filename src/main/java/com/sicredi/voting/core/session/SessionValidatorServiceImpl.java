package com.sicredi.voting.core.session;

import com.sicredi.voting.config.exception.BadRequestException;
import com.sicredi.voting.config.exception.NotFoundException;
import com.sicredi.voting.infrastructure.associate.AssociateDAO;
import com.sicredi.voting.infrastructure.session.SessionDAO;
import com.sicredi.voting.infrastructure.subject.SubjectDAO;
import com.sicredi.voting.infrastructure.user.UserDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionValidatorServiceImpl implements SessionValidatorService {

    private final SessionDAO sessionDAO;
    private final AssociateDAO associateDAO;
    private final SubjectDAO subjectDAO;
    private final UserDAO userDAO;

    @Override
    public Mono<SessionBO> validateSession(Mono<SessionBO> sessionBO) {
        return sessionBO.filterWhen(it -> subjectDAO.existsByCode(it.getSubjectCode()))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("Subject not found"))))
            .filterWhen(it -> isValidExpirationDate(it.getExpirationDate()))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestException("Invalid expiration date"))))
            .filterWhen(it -> BooleanUtils.not(sessionDAO.existsBySubjectCode(it.getSubjectCode())))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestException("Session already registered"))));
    }

    @Override
    public Mono<SessionVoteBO> validateSessionVote(Mono<SessionVoteBO> sessionVoteBO) {
        return sessionVoteBO
            .doOnNext(it -> log.debug("Validating session for voting"))
            .filterWhen(it -> sessionDAO.existsBySubjectCode(it.getSubjectCode()))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("Session not found"))))
            .filterWhen(it -> associateDAO.existsByCpf(it.getCpf()))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestException("Invalid associate"))))
            .filterWhen(it -> sessionDAO.findBySubjectCode(it.getSubjectCode()).map(SessionBO::getExpirationDate).map(this::isSessionActive))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestException("Session already closed for voting"))))
            .filterWhen(it -> BooleanUtils.not(sessionDAO.existsAssociateVote(it.getSubjectCode(), it.getCpf())))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestException("Associate already voted"))))
            .filterWhen(it -> userDAO.isAbleToVote(it.getCpf()))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestException("Associate unable to vote"))));
    }

    private Mono<Boolean> isValidExpirationDate(LocalDateTime expirationDate) {
        return Mono.just(Objects.nonNull(expirationDate) && expirationDate.isAfter(LocalDateTime.now()));
    }

    private boolean isSessionActive(LocalDateTime expirationDate) {
        return LocalDateTime.now().isBefore(expirationDate);
    }
}
