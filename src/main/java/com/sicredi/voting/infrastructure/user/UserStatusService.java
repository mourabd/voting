package com.sicredi.voting.infrastructure.user;

import reactor.core.publisher.Mono;

public interface UserStatusService {

    Mono<UserStatusResponseDTO> findStatusByCpf(String cpf);
}
