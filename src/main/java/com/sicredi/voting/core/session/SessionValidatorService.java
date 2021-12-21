package com.sicredi.voting.core.session;

import reactor.core.publisher.Mono;

public interface SessionValidatorService {

    Mono<SessionBO> validateSession(Mono<SessionBO> sessionBO);

    Mono<SessionVoteBO> validateSessionVote(Mono<SessionVoteBO> sessionVoteBO);
}
