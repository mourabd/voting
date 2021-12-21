package com.sicredi.voting.infrastructure.subject;

import com.sicredi.voting.core.subject.SubjectBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SubjectDAO {

    private final SubjectRepository subjectRepository;

    public Mono<Boolean> existsByCode(String code) {
        return subjectRepository.existsByCode(code)
            .doOnNext(exists -> log.debug("Exists subject: {}", exists));
    }

    public Mono<SubjectBO> findByCode(String code) {
        return subjectRepository.findByCode(code)
            .map(SubjectMapper::toBusinessObject);
    }

    public Flux<SubjectBO> findAll() {
        return subjectRepository.findAll(Sort.by(Sort.Direction.DESC, "creationDateTime"))
            .map(SubjectMapper::toBusinessObject);
    }

    public Mono<SubjectBO> save(Mono<SubjectBO> subjectBO) {
        return subjectBO.map(SubjectMapper::toDocument)
            .flatMap(subjectRepository::save)
            .map(SubjectMapper::toBusinessObject);
    }
}
