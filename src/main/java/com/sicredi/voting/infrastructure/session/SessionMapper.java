package com.sicredi.voting.infrastructure.session;

import com.sicredi.voting.core.session.SessionBO;
import com.sicredi.voting.core.session.SessionStatusEnum;
import com.sicredi.voting.core.session.SessionVoteBO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SessionMapper {

    public static SessionDocument toDocument(SessionBO sessionBO) {
        return SessionDocument.builder()
            .subjectCode(sessionBO.getSubjectCode())
            .expirationDate(sessionBO.getExpirationDate())
            .build();
    }

    public static SessionBO toBusinessObject(SessionDocument sessionDocument) {
        return SessionBO.builder()
            .status(SessionStatusEnum.fromExpirationDate(sessionDocument.getExpirationDate()))
            .subjectCode(sessionDocument.getSubjectCode())
            .expirationDate(sessionDocument.getExpirationDate())
            .votes(toBusinessObject(sessionDocument.getVotes()))
            .build();
    }

    public static SessionDocument.VoteDocument toDocument(SessionVoteBO sessionVoteBO) {
        return SessionDocument.VoteDocument.builder()
            .cpf(sessionVoteBO.getCpf())
            .option(sessionVoteBO.getOption())
            .build();
    }

    private static List<SessionBO.VoteBO> toBusinessObject(List<SessionDocument.VoteDocument> voteDocuments) {
        if (CollectionUtils.isEmpty(voteDocuments)) {
            return Collections.emptyList();
        }
        final List<SessionBO.VoteBO> voteBOS = new ArrayList<>(voteDocuments.size());
        for (SessionDocument.VoteDocument voteDocument : voteDocuments) {
            voteBOS.add(toBusinessObject(voteDocument));
        }
        return voteBOS;
    }

    private static SessionBO.VoteBO toBusinessObject(SessionDocument.VoteDocument voteDocument) {
        return SessionBO.VoteBO.builder()
            .cpf(voteDocument.getCpf())
            .option(voteDocument.getOption())
            .build();
    }
}
