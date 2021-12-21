package com.sicredi.voting.core.session;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class SessionVoteBO {
    private final String subjectCode;
    private final String cpf;
    private final Boolean option;
}
