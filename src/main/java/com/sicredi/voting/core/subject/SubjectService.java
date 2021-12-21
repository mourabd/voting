package com.sicredi.voting.core.subject;

import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SubjectService {

    Mono<SubjectBO> findByCode(@NonNull String code);

    Flux<SubjectBO> findAll();

    Mono<SubjectBO> save(Mono<SubjectBO> subjectBO);
}
