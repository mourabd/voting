package com.sicredi.voting.core.associate;

import com.sicredi.voting.config.exception.BadRequestException;
import com.sicredi.voting.config.exception.NotFoundException;
import com.sicredi.voting.infrastructure.associate.AssociateDAO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssociateServiceImpl implements AssociateService {

    private final AssociateDAO associateDAO;

    @Override
    public Mono<AssociateBO> findByCpf(@NonNull String cpf) {
        log.debug("Searching associate by cpf {}", cpf);
        return associateDAO.findByCpf(cpf)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("Associate not found"))));
    }

    @Override
    public Flux<AssociateBO> findAll() {
        log.debug("Retrieving all registered associates");
        return associateDAO.findAll();
    }

    @Override
    public Mono<AssociateBO> save(Mono<AssociateBO> associateBO) {
        return associateBO
            .doOnNext(it -> log.debug("Saving new associate from associate business object: {}", it))
            .filterWhen(it -> BooleanUtils.not(associateDAO.existsByCpf(it.getCpf())))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestException("Associate already registered"))))
            .flatMap(it -> associateDAO.save(Mono.just(it)));
    }
}
