package com.sicredi.voting.api.subject;

import com.sicredi.voting.core.subject.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/voting-service/v1/subjects", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubjectController {

    private final SubjectService subjectService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "Searches subject by its code")
    @Parameter(in = ParameterIn.PATH, name = "code", required = true, example = "SUBJECT-CODE")
    @GetMapping(value = "/{code}")
    public Mono<SubjectResponseDTO> findByCode(@NotBlank @PathVariable("code") final String code) {
        log.info("Searching subject by code {}", code);
        return subjectService.findByCode(code)
            .map(SubjectApiMapper::toDataTransferObject)
            .doOnNext(response -> log.debug("Subject was found: {}", response));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "Retrieves all registered subjects")
    @GetMapping
    public Flux<SubjectResponseDTO> findAll() {
        log.info("Retrieving all registered subjects");
        return subjectService.findAll()
            .map(SubjectApiMapper::toDataTransferObject);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "Creates a new subject to be voted by associates")
    @PostMapping
    public Mono<SubjectResponseDTO> save(@Valid @RequestBody Mono<SubjectRequestDTO> subjectRequestDTO) {
        return subjectRequestDTO
            .doOnNext(it -> log.info("Subject data transfer object to be persisted: {}", it))
            .map(SubjectApiMapper::toBusinessObject)
            .flatMap(it -> subjectService.save(Mono.just(it)))
            .map(SubjectApiMapper::toDataTransferObject)
            .doOnNext(response -> log.debug("Subject data transfer object: {}", response));
    }
}
