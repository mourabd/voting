package com.sicredi.voting.infrastructure.associate;

import com.sicredi.voting.core.associate.AssociateBO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AssociateDAO {

    private final AssociateRepository associateRepository;

    public Mono<Boolean> existsByCpf(@NonNull String cpf) {
        return associateRepository.existsByCpf(cpf)
            .doOnNext(exists -> log.debug("Exists associate: {}", exists));
    }

    public Mono<AssociateBO> findByCpf(@NonNull String cpf) {
        return associateRepository.findByCpf(cpf)
            .map(AssociateMapper::toBusinessObject)
            .doOnNext(associateBO -> log.debug("Associate business object: {}", associateBO));
    }

    public Flux<AssociateBO> findAll() {
        return associateRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName", "lastName"))
            .map(AssociateMapper::toBusinessObject);
    }

    public Mono<AssociateBO> save(Mono<AssociateBO> associateBO) {
        return associateBO
            .map(AssociateMapper::toDocument)
            .flatMap(associateRepository::save)
            .map(AssociateMapper::toBusinessObject);
    }
}
