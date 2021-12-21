package com.sicredi.voting.core.session;

import com.sicredi.voting.config.exception.NotFoundException;
import com.sicredi.voting.infrastructure.session.SessionDAO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {

    private final SessionDAO sessionDAO;
    private final SessionValidatorService sessionValidatorService;

    @Override
    public Mono<SessionBO> findBySubjectCode(@NonNull String subjectCode) {
        log.debug("Searching voting session by subject code {}", subjectCode);
        return sessionDAO.findBySubjectCode(subjectCode)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("Session not found"))));
    }

    @Override
    public Flux<SessionBO> findAll() {
        log.debug("Retrieving all voting sessions");
        return sessionDAO.findAll();
    }

    @Override
    public Mono<SessionBO> save(Mono<SessionBO> sessionBO) {
        return sessionBO
            .doOnNext(it -> log.debug("Saving new voting session from voting session business object: {}", it))
            .map(this::updateExpirationDate)
            .flatMap(it -> sessionValidatorService.validateSession(Mono.just(it)))
            .flatMap(it -> sessionDAO.save(Mono.just(it)));
    }

    @Override
    public Mono<SessionBO> updateVote(Mono<SessionVoteBO> sessionVoteBO) {
        return sessionValidatorService.validateSessionVote(sessionVoteBO)
            .doOnNext(it -> log.debug("Adding new vote from voting session business object: {}", it))
            .flatMap(it -> sessionDAO.update(Mono.just(it)));
    }

    @Override
    public Mono<SessionResultBO> findResultBySubjectCode(@NonNull String subjectCode) {
        log.debug("Searching voting session result by subject code {}", subjectCode);
        return sessionDAO.findBySubjectCode(subjectCode)
            .map(this::convertToSessionResultBO)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("Session not found"))));
    }

    private SessionBO updateExpirationDate(SessionBO sessionBO) {
        return Objects.isNull(sessionBO.getExpirationDate()) ? sessionBO.withExpirationDate(LocalDateTime.now().plusMinutes(1L)) : sessionBO;
    }

    private SessionResultBO convertToSessionResultBO(SessionBO sessionBO) {
        return SessionResultBO.builder()
            .session(sessionBO)
            .result(buildResultBO(sessionBO.getVotes()))
            .build();
    }

    private static SessionResultBO.ResultBO buildResultBO(List<SessionBO.VoteBO> votes) {
        final Map<Boolean, Long> countingVotesMap = votes.stream()
            .map(SessionBO.VoteBO::getOption)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return SessionResultBO.ResultBO.builder()
            .yes(Optional.ofNullable(countingVotesMap.get(Boolean.TRUE)).orElse(0L))
            .no(Optional.ofNullable(countingVotesMap.get(Boolean.FALSE)).orElse(0L))
            .build();
    }
}
