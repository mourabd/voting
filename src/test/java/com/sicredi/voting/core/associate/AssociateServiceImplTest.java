package com.sicredi.voting.core.associate;

import com.sicredi.voting.api.associate.AssociateResponseDTO;
import com.sicredi.voting.api.associate.AssociateResponseDTOStub;
import com.sicredi.voting.config.exception.BadRequestException;
import com.sicredi.voting.config.exception.NotFoundException;
import com.sicredi.voting.infrastructure.associate.AssociateDAO;
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
public class AssociateServiceImplTest {

    private static final String CPF = "CPF";

    @Mock
    private AssociateDAO associateDAO;

    @InjectMocks
    private AssociateServiceImpl associateService;

    @Test
    public void shouldFindAssociateByCpf() {

        // given
        final AssociateBO expectedResponse = AssociateBOStub.create();
        final AssociateBO associateBO = AssociateBOStub.create();

        // when
        when(associateDAO.findByCpf(CPF)).thenReturn(Mono.just(associateBO));

        // then
        final AssociateBO response = associateService.findByCpf(CPF).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCpf(), response.getCpf()),
            () -> assertEquals(expectedResponse.getFirstName(), response.getFirstName()),
            () -> assertEquals(expectedResponse.getLastName(), response.getLastName())
        );
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenAssociateIsNotFound() {

        // when
        when(associateDAO.findByCpf(CPF)).thenReturn(Mono.error(new NotFoundException("Associate not found")));

        // then
        StepVerifier
            .create(associateService.findByCpf(CPF))
            .expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().equals("Associate not found"))
            .verify();
    }

    @Test
    public void shouldFindAllRegisteredAssociates() {

        // given
        final int FIRST_ELEMENT = 0;
        final AssociateBO associateBO = AssociateBOStub.create();
        final List<AssociateBO> expectedResponse = Collections.singletonList(associateBO);

        // when
        when(associateDAO.findAll()).thenReturn(Flux.just(associateBO));

        // then
        final List<AssociateBO> response = associateService.findAll().collectList().block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.size(), response.size()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getCpf(), response.get(FIRST_ELEMENT).getCpf()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getFirstName(), response.get(FIRST_ELEMENT).getFirstName()),
            () -> assertEquals(expectedResponse.get(FIRST_ELEMENT).getLastName(), response.get(FIRST_ELEMENT).getLastName())
        );
    }

    @Test
    public void shouldSaveAssociate() {

        // given
        final AssociateBO associateBO = AssociateBOStub.create();
        final Mono<AssociateBO> associateBOMono = Mono.just(associateBO);
        final AssociateResponseDTO expectedResponse = AssociateResponseDTOStub.create();

        // when
        when(associateDAO.existsByCpf(associateBO.getCpf())).thenReturn(Mono.just(Boolean.FALSE));
        when(associateDAO.save(any())).thenReturn(associateBOMono);

        // then
        final AssociateBO response = associateService.save(associateBOMono).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCpf(), response.getCpf()),
            () -> assertEquals(expectedResponse.getFirstName(), response.getFirstName()),
            () -> assertEquals(expectedResponse.getLastName(), response.getLastName())
        );
    }

    @Test
    public void shouldNotSaveAssociateWhenAlreadyExists() {

        // given
        final AssociateBO associateBO = AssociateBOStub.create();
        final Mono<AssociateBO> associateBOMono = Mono.just(associateBO);

        // when
        when(associateDAO.existsByCpf(associateBO.getCpf())).thenReturn(Mono.just(Boolean.TRUE));

        // then
        StepVerifier
            .create(associateService.save(associateBOMono))
            .expectErrorMatches(throwable -> throwable instanceof BadRequestException && throwable.getMessage().equals("Associate already registered"))
            .verify();
    }
}
