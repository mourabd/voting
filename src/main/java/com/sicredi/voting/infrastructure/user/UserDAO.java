package com.sicredi.voting.infrastructure.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDAO {

    private final UserStatusService userStatusService;

    public Mono<Boolean> isAbleToVote(String cpf) {
        log.debug("Checking if user {} is able to vote", cpf);
        return userStatusService.findStatusByCpf(cpf)
            .doOnNext(userStatusResponseDTO -> log.debug("User status response data transfer object: {}", userStatusResponseDTO))
            .map(UserStatusResponseDTO::getStatus)
            .map(UserStatusEnum.ABLE_TO_VOTE::equals)
            .doOnNext(status -> log.debug("User able to vote: {}", status));
    }
}
