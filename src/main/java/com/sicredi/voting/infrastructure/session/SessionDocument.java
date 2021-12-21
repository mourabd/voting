package com.sicredi.voting.infrastructure.session;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Document("votingSession")
public class SessionDocument {

    @Id
    private final String id;

    private final String subjectCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime expirationDate;

    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime creationDateTime = LocalDateTime.now();

    @With
    private List<VoteDocument> votes;

    @Getter
    @Builder
    public static class VoteDocument {

        @Id
        private final String cpf;
        private final Boolean option;
    }
}
