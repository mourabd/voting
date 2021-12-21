package com.sicredi.voting.infrastructure.session;

import com.sicredi.voting.core.session.SessionBO;
import com.sicredi.voting.core.session.SessionVoteBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SessionDAO {

    private final SessionRepository sessionRepository;

    public Mono<Boolean> existsBySubjectCode(String subjectCode) {
        return sessionRepository.existsBySubjectCode(subjectCode);
    }

    public Mono<Boolean> existsAssociateVote(String subjectCode, String cpf) {
        return sessionRepository.existsBySubjectCodeAndVotesCpf(subjectCode, cpf)
            .doOnNext(exists -> log.debug("Exists associate vote: {}", exists));
    }

    public Mono<SessionBO> findBySubjectCode(String subjectCode) {
        return sessionRepository.findBySubjectCode(subjectCode)
            .map(SessionMapper::toBusinessObject)
            .doOnNext(sessionBO -> log.debug("Session business object: {}", sessionBO));
    }

    public Flux<SessionBO> findAll() {
        return sessionRepository.findAll(Sort.by(Sort.Direction.DESC, "expirationDate"))
            .map(SessionMapper::toBusinessObject);
    }

    public Mono<SessionBO> save(Mono<SessionBO> sessionBO) {
        return sessionBO.map(SessionMapper::toDocument)
            .flatMap(sessionRepository::save)
            .map(SessionMapper::toBusinessObject);
    }

    public Mono<SessionBO> update(Mono<SessionVoteBO> sessionVoteBO) {
        return sessionVoteBO.flatMap(this::fetchUpdatedSessionDocument)
            .flatMap(sessionRepository::save)
            .map(SessionMapper::toBusinessObject);
    }

    private Mono<SessionDocument> fetchUpdatedSessionDocument(SessionVoteBO sessionVoteBO) {
        return sessionRepository.findBySubjectCode(sessionVoteBO.getSubjectCode())
            .map(sessionDocument -> updateSessionVoteList(SessionMapper.toDocument(sessionVoteBO), sessionDocument));
    }

    private SessionDocument updateSessionVoteList(SessionDocument.VoteDocument voteDocument, SessionDocument sessionDocument) {
        if (CollectionUtils.isEmpty(sessionDocument.getVotes())) {
            return sessionDocument.withVotes(Collections.singletonList(voteDocument));
        }
        final List<SessionDocument.VoteDocument> updatedVotes = new ArrayList<>(sessionDocument.getVotes());
        updatedVotes.add(voteDocument);
        return sessionDocument.withVotes(updatedVotes);
    }
}
