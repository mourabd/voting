package com.sicredi.voting.infrastructure.session;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface SessionRepository extends ReactiveMongoRepository<SessionDocument, String> {

    Mono<Boolean> existsBySubjectCode(String subjectCode);

    Mono<Boolean> existsBySubjectCodeAndVotesCpf(String subjectCode, String cpf);

    Mono<SessionDocument> findBySubjectCode(String subjectCode);
}
