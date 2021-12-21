package com.sicredi.voting.core.associate;

import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AssociateService {

    Mono<AssociateBO> findByCpf(@NonNull String cpf);

    Flux<AssociateBO> findAll();

    Mono<AssociateBO> save(Mono<AssociateBO> associateBO);
}
