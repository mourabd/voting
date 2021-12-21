package com.sicredi.voting.infrastructure.associate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@Document("associate")
public class AssociateDocument {

    @Id
    private String id;
    private final String firstName;
    private final String lastName;
    private final String cpf;

    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime creationDateTime = LocalDateTime.now();
}
