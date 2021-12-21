package com.sicredi.voting.api.subject;

import com.sicredi.voting.core.subject.SubjectBOStub;
import com.sicredi.voting.core.subject.SubjectBO;
import com.sicredi.voting.core.subject.SubjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubjectControllerTest {

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    @Test
    public void shouldFindSubjectByCode() {

        // given
        final String CODE = "CODE";
        final SubjectResponseDTO expectedResponse = SubjectResponseDTOStub.create();
        final SubjectBO subjectBO = SubjectBOStub.create();

        // when
        when(subjectService.findByCode(CODE)).thenReturn(Mono.just(subjectBO));

        // then
        final SubjectResponseDTO response = subjectController.findByCode(CODE).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCode(), response.getCode()),
            () -> assertEquals(expectedResponse.getTitle(), response.getTitle()),
            () -> assertEquals(expectedResponse.getDescription(), response.getDescription())
        );
    }

    @Test
    public void shouldFindAllRegisteredSubjects() {

        // given
        final int FIRST_ELEMENT = 0;
        final List<SubjectResponseDTO> expectedResponse = Collections.singletonList(SubjectResponseDTOStub.create());
        final SubjectBO subjectBO = SubjectBOStub.create();

        // when
        when(subjectService.findAll()).thenReturn(Flux.just(subjectBO));

        // then
        final List<SubjectResponseDTO> response = subjectController.findAll().collectList().block();

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
        final SubjectRequestDTO subjectRequestDTO = SubjectRequestDTOStub.create();
        final SubjectBO subjectBO = SubjectBOStub.create();
        final SubjectResponseDTO expectedResponse = SubjectResponseDTOStub.create();

        // when
        when(subjectService.save(any())).thenReturn(Mono.just(subjectBO));

        // then
        final SubjectResponseDTO response = subjectController.save(Mono.just(subjectRequestDTO)).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCode(), response.getCode()),
            () -> assertEquals(expectedResponse.getTitle(), response.getTitle()),
            () -> assertEquals(expectedResponse.getDescription(), response.getDescription())
        );

    }
}
