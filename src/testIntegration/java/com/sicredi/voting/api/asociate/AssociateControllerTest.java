package com.sicredi.voting.api.asociate;

import com.sicredi.voting.api.associate.AssociateController;
import com.sicredi.voting.api.associate.AssociateRequestDTO;
import com.sicredi.voting.core.associate.AssociateServiceImpl;
import com.sicredi.voting.infrastructure.associate.AssociateDAO;
import com.sicredi.voting.infrastructure.associate.AssociateDocument;
import com.sicredi.voting.infrastructure.associate.AssociateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(AssociateController.class)
@Import({AssociateServiceImpl.class, AssociateDAO.class})
public class AssociateControllerTest {

    private static final String FIRST_NAME = "Bruno";
    private static final String LAST_NAME = "Testes";
    private static final String URI = "/api/voting-service/v1/associates";

    @MockBean
    AssociateRepository repository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldCreateAssociate() {

        // given
        final String randomCpf = String.valueOf(generateId());
        final AssociateRequestDTO associateRequestDTO = buildAssociateRequestDTO(randomCpf, FIRST_NAME, LAST_NAME);
        final AssociateDocument associateDocument = buildAssociateDocument(randomCpf, FIRST_NAME, LAST_NAME);

        // when
        when(repository.existsByCpf(randomCpf)).thenReturn(Mono.just(Boolean.FALSE));
        when(repository.save(any(AssociateDocument.class))).thenReturn(Mono.just(associateDocument));

        // then
        webTestClient.post().uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(associateRequestDTO), AssociateRequestDTO.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.cpf").isEqualTo(randomCpf)
            .jsonPath("$.firstName").isEqualTo(FIRST_NAME)
            .jsonPath("$.lastName").isEqualTo(LAST_NAME);
    }

    @Test
    public void shouldNotCreateAssociateWhenCpfFormatIsNotValid() {

        // given
        final String randomCpf = String.valueOf(generateId());
        final AssociateRequestDTO associateRequestDTO = buildAssociateRequestDTO(randomCpf, "", LAST_NAME);

        // then
        webTestClient.post().uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(associateRequestDTO), AssociateRequestDTO.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotCreateAssociateWhenFirstNameIsBlank() {

        // given
        final AssociateRequestDTO associateRequestDTO = buildAssociateRequestDTO("123", FIRST_NAME, LAST_NAME);

        // then
        webTestClient.post().uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(associateRequestDTO), AssociateRequestDTO.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotCreateAssociateWhenLastNameIsBlank() {

        // given
        final String randomCpf = String.valueOf(generateId());
        final AssociateRequestDTO associateRequestDTO = buildAssociateRequestDTO(randomCpf, FIRST_NAME, null);

        // then
        webTestClient.post().uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(associateRequestDTO), AssociateRequestDTO.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void shouldFindAssociateByCpf() {

        // given
        final String randomCpf = String.valueOf(generateId());
        final AssociateDocument associateDocument = buildAssociateDocument(randomCpf, FIRST_NAME, LAST_NAME);

        // when
        when(repository.findByCpf(randomCpf)).thenReturn(Mono.just(associateDocument));

        // then
        webTestClient.get().uri(URI.concat("/{cpf}"), randomCpf)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.cpf").isEqualTo(randomCpf)
            .jsonPath("$.firstName").isEqualTo(FIRST_NAME)
            .jsonPath("$.lastName").isEqualTo(LAST_NAME);
    }

    @Test
    public void shouldNotFindAssociateByCpf() {

        // given
        final String randomCpf = String.valueOf(generateId());

        // when
        when(repository.findByCpf(randomCpf)).thenReturn(Mono.empty());

        // then
        webTestClient.get().uri(URI.concat("/{cpf}"), randomCpf)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void shouldReturnRegisteredAssociates() {

        // given
        final String randomCpf = String.valueOf(generateId());
        final AssociateDocument associateDocument = buildAssociateDocument(randomCpf, FIRST_NAME, LAST_NAME);

        // when
        when(repository.findAll(Sort.by(Sort.Direction.ASC, "firstName", "lastName"))).thenReturn(Flux.just(associateDocument));

        // then
        webTestClient.get().uri(URI)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[0].cpf").isEqualTo(randomCpf)
            .jsonPath("$[0].firstName").isEqualTo(FIRST_NAME)
            .jsonPath("$[0].lastName").isEqualTo(LAST_NAME);
    }

    private AssociateRequestDTO buildAssociateRequestDTO(String cpf, String firstName, String lastName) {
        return AssociateRequestDTO.builder()
            .cpf(cpf)
            .firstName(firstName)
            .lastName(lastName)
            .build();
    }

    private AssociateDocument buildAssociateDocument(String cpf, String firstName, String lastName) {
        return AssociateDocument.builder()
            .cpf(cpf)
            .firstName(firstName)
            .lastName(lastName)
            .build();
    }

    private long generateId() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextLong(10_000_000_000L, 100_000_000_000L);
    }
}
