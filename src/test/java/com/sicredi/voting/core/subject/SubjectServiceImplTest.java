package com.sicredi.voting.core.subject;

import com.sicredi.voting.api.subject.SubjectResponseDTO;
import com.sicredi.voting.api.subject.SubjectResponseDTOStub;
import com.sicredi.voting.config.exception.BadRequestException;
import com.sicredi.voting.config.exception.NotFoundException;
import com.sicredi.voting.infrastructure.subject.SubjectDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceImplTest {

    private static final String SUBJECT_CODE = "SUBJECT_CODE";

    @Mock
    private SubjectDAO subjectDAO;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @Test
    public void shouldFindSubjectByCode() {

        // given
        final SubjectBO expectedResponse = SubjectBOStub.create();
        final SubjectBO subjectBO = SubjectBOStub.create();

        // when
        when(subjectDAO.findByCode(SUBJECT_CODE)).thenReturn(Mono.just(subjectBO));

        // then
        final SubjectBO response = subjectService.findByCode(SUBJECT_CODE).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCode(), response.getCode()),
            () -> assertEquals(expectedResponse.getTitle(), response.getTitle()),
            () -> assertEquals(expectedResponse.getDescription(), response.getDescription())
        );
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenSubjectIsNotFound() {

        // when
        when(subjectDAO.findByCode(SUBJECT_CODE)).thenReturn(Mono.error(new NotFoundException("Subject not found")));

        // then
        StepVerifier
            .create(subjectService.findByCode(SUBJECT_CODE))
            .expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().equals("Subject not found"))
            .verify();
    }

    @Test
    public void shouldFindAllRegisteredSubjects() {

        // given
        final int FIRST_ELEMENT = 0;
        final SubjectBO subjectBO = SubjectBOStub.create();
        final List<SubjectBO> expectedResponse = Collections.singletonList(subjectBO);

        // when
        when(subjectDAO.findAll()).thenReturn(Flux.just(subjectBO));

        // then
        final List<SubjectBO> response = subjectService.findAll().collectList().block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.size(), response.size()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getCode(), response.get(FIRST_ELEMENT).getCode()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getTitle(), response.get(FIRST_ELEMENT).getTitle()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getDescription(), response.get(FIRST_ELEMENT).getDescription()
            )
        );
    }

    @Test
    public void shouldSaveSubject() {

        // given
        final SubjectBO subjectBO = SubjectBOStub.create();
        final Mono<SubjectBO> subjectBOMono = Mono.just(subjectBO);
        final SubjectResponseDTO expectedResponse = SubjectResponseDTOStub.create();

        // when
        when(subjectDAO.existsByCode(subjectBO.getCode())).thenReturn(Mono.just(Boolean.FALSE));
        when(subjectDAO.save(any())).thenReturn(subjectBOMono);

        // then
        final SubjectBO response = subjectService.save(subjectBOMono).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCode(), response.getCode()),
            () -> assertEquals(expectedResponse.getTitle(), response.getTitle()),
            () -> assertEquals(expectedResponse.getDescription(), response.getDescription())
        );
    }

    @Test
    public void shouldNotSaveSubjectWhenAlreadyExists() {

        // given
        final SubjectBO subjectBO = SubjectBOStub.create();
        final Mono<SubjectBO> subjectBOMono = Mono.just(subjectBO);

        // when
        when(subjectDAO.existsByCode(subjectBO.getCode())).thenReturn(Mono.just(Boolean.TRUE));

        // then
        StepVerifier
            .create(subjectService.save(subjectBOMono))
            .expectErrorMatches(throwable -> throwable instanceof BadRequestException && throwable.getMessage().equals("Subject already registered"))
            .verify();
    }
}
