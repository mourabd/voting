package com.sicredi.voting.infrastructure.user;

public class UserStatusResponseDTOStub {

    private UserStatusResponseDTOStub() {
    }

    public static UserStatusResponseDTO create(UserStatusEnum userStatusEnum) {
        return UserStatusResponseDTO.builder()
            .status(userStatusEnum)
            .build();
    }
}