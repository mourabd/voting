package com.sicredi.voting.infrastructure.subject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@Document("subject")
public class SubjectDocument {

    @Id
    private String id;

    private final String title;

    private final String description;

    private final String code;

    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime creationDateTime = LocalDateTime.now();
}
