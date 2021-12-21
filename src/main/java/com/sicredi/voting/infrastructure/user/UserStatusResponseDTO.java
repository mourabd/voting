package com.sicredi.voting.infrastructure.user;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class UserStatusResponseDTO {

    @NotNull(message = "Status is required.")
    private final UserStatusEnum status;
}
