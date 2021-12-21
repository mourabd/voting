package com.sicredi.voting.infrastructure.associate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AssociateRepository extends ReactiveMongoRepository<AssociateDocument, String> {

    Mono<Boolean> existsByCpf(String cpf);

    Mono<AssociateDocument> findByCpf(String cpf);
}
