package com.sicredi.voting.api.session;

import com.sicredi.voting.core.session.SessionServiceImpl;
import com.sicredi.voting.core.session.SessionValidatorServiceImpl;
import com.sicredi.voting.infrastructure.associate.AssociateDAO;
import com.sicredi.voting.infrastructure.associate.AssociateRepository;
import com.sicredi.voting.infrastructure.session.SessionDAO;
import com.sicredi.voting.infrastructure.session.SessionDocument;
import com.sicredi.voting.infrastructure.session.SessionRepository;
import com.sicredi.voting.infrastructure.subject.SubjectDAO;
import com.sicredi.voting.infrastructure.subject.SubjectRepository;
import com.sicredi.voting.infrastructure.user.UserDAO;
import com.sicredi.voting.infrastructure.user.UserStatusEnum;
import com.sicredi.voting.infrastructure.user.UserStatusResponseDTO;
import com.sicredi.voting.infrastructure.user.UserStatusServiceImpl;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.sicredi.voting.core.session.SessionStatusEnum.OPEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(SessionController.class)
@Import({SessionServiceImpl.class, SessionValidatorServiceImpl.class, SessionDAO.class, AssociateDAO.class, SubjectDAO.class, UserDAO.class})
public class SessionControllerTest {

    private static final String CPF = "CPF";
    private static final String CODE = "CODE";
    private static final LocalDateTime EXPIRATION_DATE = LocalDateTime.now().plus(Duration.ofHours(5L));
    private static final String URI = "/api/voting-service/v1/sessions";

    @MockBean
    SessionRepository sessionRepository;

    @MockBean
    AssociateRepository associateRepository;

    @MockBean
    SubjectRepository subjectRepository;

    @MockBean
    UserStatusServiceImpl userStatusService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldCreateSession() {

        // given
        final SessionRequestDTO sessionRequestDTO = buildSessionRequestDTO(CODE, null);
        final SessionDocument sessionDocument = buildSessionDocument();

        // when
        when(subjectRepository.existsByCode(CODE)).thenReturn(Mono.just(Boolean.TRUE));
        when(sessionRepository.existsBySubjectCode(CODE)).thenReturn(Mono.just(Boolean.FALSE));
        when(sessionRepository.save(any(SessionDocument.class))).thenReturn(Mono.just(sessionDocument));

        // then
        webTestClient.post().uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(sessionRequestDTO), SessionRequestDTO.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.subjectCode").isEqualTo(CODE)
            .jsonPath("$.status").isNotEmpty()
            .jsonPath("$.expirationDate").isNotEmpty();
    }

    @Test
    public void shouldNotCreateSessionWhenSubjectCodeIsBlank() {

        // given
        final SessionRequestDTO sessionRequestDTO = buildSessionRequestDTO("", LocalDateTime.now());

        // then
        webTestClient.post().uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(sessionRequestDTO), SessionRequestDTO.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotCreateSessionWhenSubjectCodeNotExists() {

        // given
        final SessionRequestDTO sessionRequestDTO = buildSessionRequestDTO(CODE, LocalDateTime.now().minus(Duration.ofDays(1L)));

        // when
        when(subjectRepository.existsByCode(CODE)).thenReturn(Mono.just(Boolean.FALSE));

        // then
        webTestClient.post().uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(sessionRequestDTO), SessionRequestDTO.class)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void shouldNotCreateSessionWhenExpirationDateIsInvalid() {

        // given
        final SessionRequestDTO sessionRequestDTO = buildSessionRequestDTO(CODE, LocalDateTime.now().minus(Duration.ofDays(1L)));

        // when
        when(subjectRepository.existsByCode(CODE)).thenReturn(Mono.just(Boolean.TRUE));

        // then
        webTestClient.post().uri(URI)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(sessionRequestDTO), SessionRequestDTO.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void shouldFindSessionBySubjectCode() {

        // given
        final SessionDocument sessionDocument = buildSessionDocument();

        // when
        when(sessionRepository.findBySubjectCode(CODE)).thenReturn(Mono.just(sessionDocument));

        // then
        webTestClient.get().uri(URI.concat("/{subjectCode}"), CODE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.subjectCode").isEqualTo(CODE)
            .jsonPath("$.status").isNotEmpty()
            .jsonPath("$.expirationDate").isNotEmpty();
    }

    @Test
    public void shouldNotFindSessionBySubjectCode() {

        // when
        when(sessionRepository.findBySubjectCode(CODE)).thenReturn(Mono.empty());

        // then
        webTestClient.get().uri(URI.concat("/{subjectCode}"), CODE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void shouldReturnRegisteredSessions() {

        // given
        final SessionDocument sessionDocument = buildSessionDocument();

        // when
        when(sessionRepository.findAll(Sort.by(Sort.Direction.DESC, "expirationDate"))).thenReturn(Flux.just(sessionDocument));

        // then
        webTestClient.get().uri(URI)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[0].subjectCode").isEqualTo(CODE)
            .jsonPath("$[0].status").isNotEmpty()
            .jsonPath("$[0].expirationDate").isNotEmpty();
    }

    @Test
    public void shouldAddSessionVote() {

        // given
        final SessionVoteRequestDTO sessionVoteRequestDTO = buildSessionVoteRequestDTO();
        final SessionDocument sessionDocument = buildSessionDocument();
        sessionDocument.withVotes(null);
        final UserStatusResponseDTO userStatusResponseDTO = buildUserStatusResponseDTO(UserStatusEnum.ABLE_TO_VOTE);

        // when
        when(sessionRepository.existsBySubjectCode(CODE)).thenReturn(Mono.just(Boolean.TRUE));
        when(associateRepository.existsByCpf(CPF)).thenReturn(Mono.just(Boolean.TRUE));
        when(sessionRepository.findBySubjectCode(CODE)).thenReturn(Mono.just(sessionDocument));
        when(sessionRepository.existsBySubjectCodeAndVotesCpf(CODE, CPF)).thenReturn(Mono.just(Boolean.FALSE));
        when(userStatusService.findStatusByCpf(CPF)).thenReturn(Mono.just(userStatusResponseDTO));
        when(sessionRepository.findBySubjectCode(CODE)).thenReturn(Mono.just(sessionDocument));
        when(sessionRepository.save(any(SessionDocument.class))).thenReturn(Mono.just(sessionDocument));

        // then
        webTestClient.post().uri(URI.concat("/{subjectCode}/vote"), CODE)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(sessionVoteRequestDTO), SessionVoteRequestDTO.class)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    public void shouldNotAddSessionVoteWhenSessionIsNotFoundBySubjectCode() {

        // given
        final SessionVoteRequestDTO sessionVoteRequestDTO = buildSessionVoteRequestDTO();

        // when
        when(sessionRepository.existsBySubjectCode(CODE)).thenReturn(Mono.just(Boolean.FALSE));

        // then
        webTestClient.post().uri(URI.concat("/{subjectCode}/vote"), CODE)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(sessionVoteRequestDTO), SessionVoteRequestDTO.class)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void shouldNotAddSessionVoteWhenAssociateIsNotRegistered() {

        // given
        final SessionVoteRequestDTO sessionVoteRequestDTO = buildSessionVoteRequestDTO();

        // when
        when(sessionRepository.existsBySubjectCode(CODE)).thenReturn(Mono.just(Boolean.TRUE));
        when(associateRepository.existsByCpf(CPF)).thenReturn(Mono.just(Boolean.FALSE));
        when(sessionRepository.findBySubjectCode(CODE)).thenReturn(Mono.empty());

        // then
        webTestClient.post().uri(URI.concat("/{subjectCode}/vote"), CODE)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(sessionVoteRequestDTO), SessionVoteRequestDTO.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotAddSessionVoteWhenAssociateAlreadyVoted() {

        // given
        final SessionVoteRequestDTO sessionVoteRequestDTO = buildSessionVoteRequestDTO();
        final SessionDocument sessionDocument = buildSessionDocument();

        // when
        when(sessionRepository.existsBySubjectCode(CODE)).thenReturn(Mono.just(Boolean.TRUE));
        when(associateRepository.existsByCpf(CPF)).thenReturn(Mono.just(Boolean.TRUE));
        when(sessionRepository.findBySubjectCode(CODE)).thenReturn(Mono.just(sessionDocument));
        when(sessionRepository.existsBySubjectCodeAndVotesCpf(CODE, CPF)).thenReturn(Mono.just(Boolean.TRUE));

        // then
        webTestClient.post().uri(URI.concat("/{subjectCode}/vote"), CODE)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(sessionVoteRequestDTO), SessionVoteRequestDTO.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotAddSessionVoteWhenUserIsUnableToVote() {

        // given
        final SessionVoteRequestDTO sessionVoteRequestDTO = buildSessionVoteRequestDTO();
        final SessionDocument sessionDocument = buildSessionDocument();
        final UserStatusResponseDTO userStatusResponseDTO = buildUserStatusResponseDTO(UserStatusEnum.UNABLE_TO_VOTE);

        // when
        when(sessionRepository.existsBySubjectCode(CODE)).thenReturn(Mono.just(Boolean.TRUE));
        when(associateRepository.existsByCpf(CPF)).thenReturn(Mono.just(Boolean.TRUE));
        when(sessionRepository.findBySubjectCode(CODE)).thenReturn(Mono.just(sessionDocument));
        when(sessionRepository.existsBySubjectCodeAndVotesCpf(CODE, CPF)).thenReturn(Mono.just(Boolean.FALSE));
        when(userStatusService.findStatusByCpf(CPF)).thenReturn(Mono.just(userStatusResponseDTO));

        // then
        webTestClient.post().uri(URI.concat("/{subjectCode}/vote"), CODE)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(sessionVoteRequestDTO), SessionVoteRequestDTO.class)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    public void shouldFindSessionResultBySubjectCode() {

        // given
        final SessionDocument sessionDocument = buildSessionDocument();

        // when
        when(sessionRepository.findBySubjectCode(CODE)).thenReturn(Mono.just(sessionDocument));

        // then
        webTestClient.get().uri(URI.concat("/{subjectCode}/result"), CODE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.session.status").isEqualTo(OPEN.name())
            .jsonPath("$.session.subjectCode").isEqualTo(CODE)
            .jsonPath("$.session.expirationDate").isNotEmpty()
            .jsonPath("$.result.yes").isEqualTo(1)
            .jsonPath("$.result.no").isEqualTo(0);
    }

    private SessionRequestDTO buildSessionRequestDTO(String subjectCode, LocalDateTime expirationDate) {
        return SessionRequestDTO.builder()
            .subjectCode(subjectCode)
            .expirationDate(expirationDate)
            .build();
    }

    private SessionVoteRequestDTO buildSessionVoteRequestDTO() {
        return SessionVoteRequestDTO.builder()
            .cpf(CPF)
            .option(Boolean.TRUE)
            .build();
    }

    private UserStatusResponseDTO buildUserStatusResponseDTO(UserStatusEnum userStatusEnum) {
        return UserStatusResponseDTO.builder()
            .status(userStatusEnum)
            .build();
    }

    private SessionDocument buildSessionDocument() {
        return SessionDocument.builder()
            .subjectCode(CODE)
            .expirationDate(EXPIRATION_DATE)
            .votes(Collections.singletonList(buildVoteDocument()))
            .build();
    }

    private SessionDocument.VoteDocument buildVoteDocument() {
        return SessionDocument.VoteDocument.builder()
            .cpf(CPF)
            .option(Boolean.TRUE)
            .build();
    }
}
