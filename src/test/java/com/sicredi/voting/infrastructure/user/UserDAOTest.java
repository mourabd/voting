package com.sicredi.voting.infrastructure.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDAOTest {

    private static final String CPF = "CPF";

    @Mock
    private UserStatusService userStatusService;

    @InjectMocks
    private UserDAO userDAO;

    @Test
    public void shouldReturnTrueWhenUserIsAbleToVote() {

        // given
        final UserStatusResponseDTO userStatusResponseDTO = UserStatusResponseDTOStub.create(UserStatusEnum.ABLE_TO_VOTE);

        // when
        when(userStatusService.findStatusByCpf(CPF)).thenReturn(Mono.just(userStatusResponseDTO));

        // then
        assertTrue(userDAO.isAbleToVote(CPF).block());
    }

    @Test
    public void shouldReturnFalseWhenUserIsUnableToVote() {

        // given
        final UserStatusResponseDTO userStatusResponseDTO = UserStatusResponseDTOStub.create(UserStatusEnum.UNABLE_TO_VOTE);

        // when
        when(userStatusService.findStatusByCpf(CPF)).thenReturn(Mono.just(userStatusResponseDTO));

        // then
        assertFalse(userDAO.isAbleToVote(CPF).block());
    }
}
