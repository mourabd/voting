package com.sicredi.voting.api.associate;

import com.sicredi.voting.core.associate.AssociateBO;
import com.sicredi.voting.core.associate.AssociateBOStub;
import com.sicredi.voting.core.associate.AssociateService;
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
public class AssociateControllerTest {

    @Mock
    private AssociateService associateService;

    @InjectMocks
    private AssociateController associateController;

    @Test
    public void shouldFindAssociateByCpf() {

        // given
        final String CPF = "CPF";
        final AssociateResponseDTO expectedResponse = AssociateResponseDTOStub.create();
        final AssociateBO associateBO = AssociateBOStub.create();

        // when
        when(associateService.findByCpf(CPF)).thenReturn(Mono.just(associateBO));

        // then
        final AssociateResponseDTO response = associateController.findByCpf(CPF).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCpf(), response.getCpf()),
            () -> assertEquals(expectedResponse.getFirstName(), response.getFirstName()),
            () -> assertEquals(expectedResponse.getLastName(), response.getLastName())
        );
    }

    @Test
    public void shouldFindAllRegisteredAssociates() {

        // given
        final int FIRST_ELEMENT = 0;
        final List<AssociateResponseDTO> expectedResponse = Collections.singletonList(AssociateResponseDTOStub.create());
        final AssociateBO associateBO = AssociateBOStub.create();

        // when
        when(associateService.findAll()).thenReturn(Flux.just(associateBO));

        // then
        final List<AssociateResponseDTO> response = associateController.findAll().collectList().block();

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
        final AssociateRequestDTO associateRequestDTO = AssociateRequestDTOStub.create();
        final AssociateBO associateBO = AssociateBOStub.create();
        final AssociateResponseDTO expectedResponse = AssociateResponseDTOStub.create();

        // when
        when(associateService.save(any())).thenReturn(Mono.just(associateBO));

        // then
        final AssociateResponseDTO response = associateController.save(Mono.just(associateRequestDTO)).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(expectedResponse.getCpf(), response.getCpf()),
            () -> assertEquals(expectedResponse.getFirstName(), response.getFirstName()),
            () -> assertEquals(expectedResponse.getLastName(), response.getLastName())
        );

    }
}
