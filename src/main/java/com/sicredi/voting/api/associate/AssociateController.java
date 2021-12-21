package com.sicredi.voting.api.associate;

import com.sicredi.voting.core.associate.AssociateService;
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
@RequestMapping(path = "/api/voting-service/v1/associates", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssociateController {

    private final AssociateService associateService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "Searches associate by cpf")
    @Parameter(in = ParameterIn.PATH, name = "cpf", required = true, example = "00000000000")
    @GetMapping(value = "/{cpf}")
    public Mono<AssociateResponseDTO> findByCpf(@NotBlank(message = "CPF is required") @PathVariable("cpf") final String cpf) {
        log.info("Searching associate by cpf {}", cpf);
        return associateService.findByCpf(cpf)
            .map(AssociateApiMapper::toDataTransferObject)
            .doOnNext(response -> log.debug("Associate was found: {}", response));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(method = "GET", summary = "Retrieves all registered associates")
    @GetMapping
    public Flux<AssociateResponseDTO> findAll() {
        log.info("Retrieving all registered associates");
        return associateService.findAll()
            .map(AssociateApiMapper::toDataTransferObject);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(method = "POST", summary = "Register associate")
    @PostMapping
    public Mono<AssociateResponseDTO> save(@Valid @RequestBody Mono<AssociateRequestDTO> associateRequestDTO) {
        return associateRequestDTO
            .doOnNext(it -> log.info("Associate request data transfer object to be persisted: {}", it))
            .map(AssociateApiMapper::toBusinessObject)
            .flatMap(it -> associateService.save(Mono.just(it)))
            .map(AssociateApiMapper::toDataTransferObject)
            .doOnNext(response -> log.debug("Associate response data transfer object: {}", response));
    }
}
