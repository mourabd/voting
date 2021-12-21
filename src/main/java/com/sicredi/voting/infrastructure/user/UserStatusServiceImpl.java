package com.sicredi.voting.infrastructure.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

    private final WebClient webClient;
    private final UserStatusConfigProperties properties;

    @Override
    public Mono<UserStatusResponseDTO> findStatusByCpf(String cpf) {
        return webClient.get()
            .uri(properties.getUrl(), cpf)
            .exchangeToMono(clientResponse -> fetchUserStatusResponseDTO(clientResponse.statusCode()));
    }

    private Mono<UserStatusResponseDTO> fetchUserStatusResponseDTO(HttpStatus httpStatus) {
        return Mono.just(UserStatusResponseDTO.builder()
            .status(httpStatus.isError() ? UserStatusEnum.UNABLE_TO_VOTE : UserStatusEnum.ABLE_TO_VOTE)
            .build());
    }
}
