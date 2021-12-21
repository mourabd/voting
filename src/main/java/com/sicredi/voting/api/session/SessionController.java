package com.sicredi.voting.api.session;

import com.sicredi.voting.core.session.SessionService;
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
@RequestMapping(path = "/api/voting-service/v1/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionController {

    private static final String SUBJECT_CODE = "subjectCode";

    private final SessionService sessionService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "Searches voting session by subject code")
    @Parameter(in = ParameterIn.PATH, name = SUBJECT_CODE, required = true, example = "SUBJECT-CODE")
    @GetMapping(value = "/{subjectCode}")
    public Mono<SessionResponseDTO> findBySubjectCode(@NotBlank @PathVariable(SUBJECT_CODE) final String subjectCode) {
        log.info("Searching voting session by subject code {}", subjectCode);
        return sessionService.findBySubjectCode(subjectCode)
            .map(SessionApiMapper::toDataTransferObject)
            .doOnNext(response -> log.debug("Voting session data transfer object: {}", response));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "Retrieves all voting sessions")
    @GetMapping
    public Flux<SessionResponseDTO> findAll() {
        log.info("Retrieving all registered voting sessions");
        return sessionService.findAll()
            .map(SessionApiMapper::toDataTransferObject);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "Opens a new voting session")
    @PostMapping
    public Mono<SessionResponseDTO> save(@Valid @RequestBody Mono<SessionRequestDTO> sessionRequestDTO) {
        return sessionRequestDTO
            .doOnNext(it -> log.info("Voting session request data transfer object to be persisted: {}", it))
            .map(SessionApiMapper::toBusinessObject)
            .flatMap(it -> sessionService.save(Mono.just(it)))
            .map(SessionApiMapper::toDataTransferObject)
            .doOnNext(response -> log.debug("Voting session response data transfer object: {}", response));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(method = "POST", summary = "Registers associate vote for a given voting session.")
    @PostMapping(value = "/{subjectCode}/vote")
    public Mono<Void> addVote(@NotBlank @PathVariable(SUBJECT_CODE) final String subjectCode,
                              @Valid @RequestBody Mono<SessionVoteRequestDTO> sessionVoteRequestDTO) {
        return sessionVoteRequestDTO
            .doOnNext(it -> log.info("Session vote request data transfer object {}", it))
            .map(it -> SessionApiMapper.toBusinessObject(subjectCode, it))
            .flatMap(it -> sessionService.updateVote(Mono.just(it)))
            .then()
            .doOnNext(response -> log.debug("Voting session response data transfer object: {}", response));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "Searches voting session result by subject code")
    @Parameter(in = ParameterIn.PATH, name = SUBJECT_CODE, required = true, example = "SUBJECT-CODE")
    @GetMapping(value = "/{subjectCode}/result")
    public Mono<SessionResultDTO> findResult(@NotBlank @PathVariable(SUBJECT_CODE) final String subjectCode) {
        log.info("Searching voting session result by subject code {}", subjectCode);
        return sessionService.findResultBySubjectCode(subjectCode)
            .map(SessionApiMapper::toDataTransferObject)
            .doOnNext(response -> log.debug("Voting session data transfer object: {}", response));
    }
}
