package com.sicredi.voting.api.session;

import com.sicredi.voting.core.session.SessionBO;
import com.sicredi.voting.core.session.SessionResultBO;
import com.sicredi.voting.core.session.SessionVoteBO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SessionApiMapper {

    public static SessionBO toBusinessObject(SessionRequestDTO sessionRequestDTO) {
        return SessionBO.builder()
            .subjectCode(sessionRequestDTO.getSubjectCode())
            .expirationDate(sessionRequestDTO.getExpirationDate())
            .build();
    }

    public static SessionVoteBO toBusinessObject(String subjectCode, SessionVoteRequestDTO sessionVoteRequestDTO) {
        return SessionVoteBO.builder()
            .subjectCode(subjectCode)
            .cpf(sessionVoteRequestDTO.getCpf())
            .option(sessionVoteRequestDTO.getOption())
            .build();
    }

    public static SessionResultDTO toDataTransferObject(SessionResultBO sessionResultBO) {
        return SessionResultDTO.builder()
            .session(toDataTransferObject(sessionResultBO.getSession()))
            .result(toDataTransferObject(sessionResultBO.getResult()))
            .build();
    }

    public static SessionResponseDTO toDataTransferObject(SessionBO sessionBO) {
        return SessionResponseDTO.builder()
            .status(sessionBO.getStatus())
            .subjectCode(sessionBO.getSubjectCode())
            .expirationDate(sessionBO.getExpirationDate())
            .build();
    }

    private static SessionResultDTO.ResultDTO toDataTransferObject(SessionResultBO.ResultBO resultBO) {
        return SessionResultDTO.ResultDTO.builder()
            .yes(resultBO.getYes())
            .no(resultBO.getNo())
            .build();
    }
}
