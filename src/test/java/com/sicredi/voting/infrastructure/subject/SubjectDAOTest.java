package com.sicredi.voting.infrastructure.subject;

import com.sicredi.voting.core.subject.SubjectBO;
import com.sicredi.voting.core.subject.SubjectBOStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubjectDAOTest {

    private static final String CODE = "CODE";

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectDAO subjectDAO;

    @Test
    public void shouldReturnTrueWhenSubjectCodeIsRegistered() {

        // when
        when(subjectRepository.existsByCode(CODE)).thenReturn(Mono.just(Boolean.TRUE));

        // then
        assertTrue(subjectDAO.existsByCode(CODE).block());
    }

    @Test
    public void shouldReturnFalseWhenSubjectCodeIsNotRegistered() {

        // when
        when(subjectRepository.existsByCode(CODE)).thenReturn(Mono.just(Boolean.FALSE));

        // then
        assertFalse(subjectDAO.existsByCode(CODE).block());
    }

    @Test
    public void shouldReturnSubjectBOWhenSubjectCodeIsRegistered() {

        // given
        final SubjectDocument subjectDocument = SubjectDocumentStub.create();
        final SubjectBO expectedResponse = SubjectBOStub.create();

        // when
        when(subjectRepository.findByCode(CODE)).thenReturn(Mono.just(subjectDocument));

        // then
        final SubjectBO response = subjectDAO.findByCode(CODE).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCode(), response.getCode()),
            () -> assertEquals(expectedResponse.getTitle(), response.getTitle()),
            () -> assertEquals(expectedResponse.getDescription(), response.getDescription())
        );
    }

    @Test
    public void shouldReturnAllRegisteredSubjectBOs() {

        // given
        final int FIRST_ELEMENT = 0;
        final SubjectDocument subjectDocument = SubjectDocumentStub.create();
        final List<SubjectBO> expectedResponse = Collections.singletonList(SubjectBOStub.create());

        // when
        when(subjectRepository.findAll(Sort.by(Sort.Direction.DESC, "creationDateTime"))).thenReturn(Flux.just(subjectDocument));

        // then
        final List<SubjectBO> response = subjectDAO.findAll().collectList().block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.size(), response.size()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getCode(), response.get(FIRST_ELEMENT).getCode()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getTitle(), response.get(FIRST_ELEMENT).getTitle()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getDescription(), response.get(FIRST_ELEMENT).getDescription())
        );
    }

    @Test
    public void shouldRegisterSubject() {

        // given
        final SubjectDocument subjectDocument = SubjectDocumentStub.create();
        final SubjectBO expectedResponse = SubjectBOStub.create();

        // when
        when(subjectRepository.save(any())).thenReturn(Mono.just(subjectDocument));

        // then
        final SubjectBO response = subjectDAO.save(Mono.just(expectedResponse)).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCode(), response.getCode()),
            () -> assertEquals(expectedResponse.getTitle(), response.getTitle()),
            () -> assertEquals(expectedResponse.getDescription(), response.getDescription())
        );
    }
}
