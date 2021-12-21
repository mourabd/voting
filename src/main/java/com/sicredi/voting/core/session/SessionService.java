package com.sicredi.voting.core.session;

import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SessionService {

    Mono<SessionBO> findBySubjectCode(@NonNull String subjectCode);

    Flux<SessionBO> findAll();

    Mono<SessionBO> save(Mono<SessionBO> sessionBO);

    Mono<SessionBO> updateVote(Mono<SessionVoteBO> sessionVoteBO);

    Mono<SessionResultBO> findResultBySubjectCode(@NonNull String subjectCode);
}
