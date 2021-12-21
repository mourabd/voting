package com.sicredi.voting.api.subject;

import com.sicredi.voting.core.subject.SubjectServiceImpl;
import com.sicredi.voting.infrastructure.subject.SubjectDAO;
import com.sicredi.voting.infrastructure.subject.SubjectDocument;
import com.sicredi.voting.infrastructure.subject.SubjectRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(SubjectController.class)
@Import({SubjectServiceImpl.class, SubjectDAO.class})
public class SubjectControllerTest {

    private static final String CODE = "CODE";
    private static final String TITLE = "TITLE";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String URI = "/api/voting-service/v1/subjects";

    @MockBean
    SubjectRepository repository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldCreateSubject() {

        // given
        final SubjectRequestDTO subjectRequestDTO = buildSubjectRequestDTO(TITLE, DESCRIPTION);
        final SubjectDocument subjectDocument = buildSubjectDocument();

        // when
        when(repository.existsByCode(CODE)).thenReturn(Mono.just(Boolean.FALSE));
        when(repository.save(any(SubjectDocument.class))).thenReturn(Mono.just(subjectDocument));

        // then
        webTestClient.post().uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(subjectRequestDTO), SubjectRequestDTO.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.code").isEqualTo(CODE)
            .jsonPath("$.title").isEqualTo(TITLE)
            .jsonPath("$.description").isEqualTo(DESCRIPTION);
    }

    @Test
    public void shouldNotCreateSubjectWhenTitleIsBlank() {

        // given
        final SubjectRequestDTO subjectRequestDTO = buildSubjectRequestDTO("", DESCRIPTION);

        // then
        webTestClient.post().uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(subjectRequestDTO), SubjectRequestDTO.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotCreateSubjectWhenDescriptionIsBlank() {

        // given
        final SubjectRequestDTO subjectRequestDTO = buildSubjectRequestDTO(TITLE, null);

        // then
        webTestClient.post().uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(subjectRequestDTO), SubjectRequestDTO.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void shouldFindSubjectByCode() {

        // given
        final SubjectDocument subjectDocument = buildSubjectDocument();

        // when
        when(repository.findByCode(CODE)).thenReturn(Mono.just(subjectDocument));

        // then
        webTestClient.get().uri(URI.concat("/{subjectCode}"), CODE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.code").isEqualTo(CODE)
            .jsonPath("$.title").isEqualTo(TITLE)
            .jsonPath("$.description").isEqualTo(DESCRIPTION);
    }

    @Test
    public void shouldNotFindSubjectByCode() {

        // when
        when(repository.findByCode(CODE)).thenReturn(Mono.empty());

        // then
        webTestClient.get().uri(URI.concat("/{subjectCode}"), CODE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void shouldReturnRegisteredSubjects() {

        // given
        final SubjectDocument subjectDocument = buildSubjectDocument();

        // when
        when(repository.findAll(Sort.by(Sort.Direction.DESC, "creationDateTime"))).thenReturn(Flux.just(subjectDocument));

        // then
        webTestClient.get().uri(URI)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[0].code").isEqualTo(CODE)
            .jsonPath("$[0].title").isEqualTo(TITLE)
            .jsonPath("$[0].description").isEqualTo(DESCRIPTION);
    }

    private SubjectRequestDTO buildSubjectRequestDTO(String title, String description) {
        return SubjectRequestDTO.builder()
            .code(CODE)
            .title(title)
            .description(description)
            .build();
    }

    private SubjectDocument buildSubjectDocument() {
        return SubjectDocument.builder()
            .code(CODE)
            .title(TITLE)
            .description(DESCRIPTION)
            .build();
    }
}
