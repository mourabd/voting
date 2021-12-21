package com.sicredi.voting.infrastructure.associate;

import com.sicredi.voting.core.associate.AssociateBO;
import com.sicredi.voting.core.associate.AssociateBOStub;
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
public class AssociateDAOTest {

    private static final String CPF = "CPF";

    @Mock
    private AssociateRepository associateRepository;

    @InjectMocks
    private AssociateDAO associateDAO;

    @Test
    public void shouldReturnTrueWhenCpfIsRegistered() {

        // when
        when(associateRepository.existsByCpf(CPF)).thenReturn(Mono.just(Boolean.TRUE));

        // then
        assertTrue(associateDAO.existsByCpf(CPF).block());
    }

    @Test
    public void shouldReturnFalseWhenCpfIsNotRegistered() {

        // when
        when(associateRepository.existsByCpf(CPF)).thenReturn(Mono.just(Boolean.FALSE));

        // then
        assertFalse(associateDAO.existsByCpf(CPF).block());
    }

    @Test
    public void shouldReturnAssociateBOWhenCpfIsRegistered() {

        // given
        final AssociateDocument associateDocument = AssociateDocumentStub.create();
        final AssociateBO expectedResponse = AssociateBOStub.create();

        // when
        when(associateRepository.findByCpf(CPF)).thenReturn(Mono.just(associateDocument));

        // then
        final AssociateBO response = associateDAO.findByCpf(CPF).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCpf(), response.getCpf()),
            () -> assertEquals(expectedResponse.getFirstName(), response.getFirstName()),
            () -> assertEquals(expectedResponse.getLastName(), response.getLastName())
        );
    }

    @Test
    public void shouldReturnAllRegisteredAssociateBOs() {

        // given
        final int FIRST_ELEMENT = 0;
        final AssociateDocument associateDocument = AssociateDocumentStub.create();
        final List<AssociateBO> expectedResponse = Collections.singletonList(AssociateBOStub.create());

        // when
        when(associateRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName", "lastName"))).thenReturn(Flux.just(associateDocument));

        // then
        final List<AssociateBO> response = associateDAO.findAll().collectList().block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.size(), response.size()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getCpf(), response.get(FIRST_ELEMENT).getCpf()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getFirstName(), response.get(FIRST_ELEMENT).getFirstName()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getLastName(), response.get(FIRST_ELEMENT).getLastName())
        );
    }

    @Test
    public void shouldRegisterAssociate() {

        // given
        final AssociateDocument associateDocument = AssociateDocumentStub.create();
        final AssociateBO expectedResponse = AssociateBOStub.create();

        // when
        when(associateRepository.save(any())).thenReturn(Mono.just(associateDocument));

        // then
        final AssociateBO response = associateDAO.save(Mono.just(expectedResponse)).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCpf(), response.getCpf()),
            () -> assertEquals(expectedResponse.getFirstName(), response.getFirstName()),
            () -> assertEquals(expectedResponse.getLastName(), response.getLastName())
        );

    }
}
