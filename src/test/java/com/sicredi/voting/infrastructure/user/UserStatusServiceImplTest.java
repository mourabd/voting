package com.sicredi.voting.infrastructure.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserStatusServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private UserStatusConfigProperties properties;

    @InjectMocks
    private UserStatusServiceImpl userStatusService;

    @Test
    public void shouldReturnUserStatusResponseDTO() {

        // given
        final String CPF = "CPF";
        final UserStatusResponseDTO userStatusResponseDTO = UserStatusResponseDTOStub.create(UserStatusEnum.ABLE_TO_VOTE);
        final var uriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        final var headersSpecMock = Mockito.mock(WebClient.RequestHeadersSpec.class);

        // when
        when(properties.getUrl()).thenReturn("https://user-info.herokuapp.com/users");
        when(webClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(properties.getUrl(), CPF)).thenReturn(headersSpecMock);
        when(headersSpecMock.exchangeToMono(any())).thenReturn(Mono.just(userStatusResponseDTO));

        // then
        final UserStatusResponseDTO response = userStatusService.findStatusByCpf(CPF).block();

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(UserStatusEnum.ABLE_TO_VOTE, response.getStatus())
        );
    }
}
