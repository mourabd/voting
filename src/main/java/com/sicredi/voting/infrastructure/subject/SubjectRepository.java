package com.sicredi.voting.infrastructure.subject;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SubjectRepository extends ReactiveMongoRepository<SubjectDocument, String> {

    Mono<Boolean> existsByCode(String code);

    Mono<SubjectDocument> findByCode(String code);
}
