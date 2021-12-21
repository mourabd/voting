package com.sicredi.voting.core.subject;

import com.sicredi.voting.config.exception.BadRequestException;
import com.sicredi.voting.config.exception.NotFoundException;
import com.sicredi.voting.infrastructure.subject.SubjectDAO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectDAO subjectDAO;

    @Override
    public Mono<SubjectBO> findByCode(@NonNull String code) {
        log.debug("Searching subject by code {}", code);
        return subjectDAO.findByCode(code)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("Subject not found"))));
    }

    @Override
    public Flux<SubjectBO> findAll() {
        log.debug("Retrieving all registered subjects");
        return subjectDAO.findAll();
    }

    @Override
    public Mono<SubjectBO> save(Mono<SubjectBO> subjectBO) {
        return subjectBO
            .doOnNext(it -> log.debug("Saving new subject from subject business object: {}", it))
            .filterWhen(it -> BooleanUtils.not(subjectDAO.existsByCode(it.getCode())))
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestException("Subject already registered"))))
            .map(this::updateSubjectBO)
            .flatMap(it -> subjectDAO.save(Mono.just(it)));
    }

    private SubjectBO updateSubjectBO(SubjectBO subjectBO) {
        return StringUtils.hasText(subjectBO.getCode()) ? subjectBO : subjectBO.withCode(UUID.randomUUID().toString());
    }
}
