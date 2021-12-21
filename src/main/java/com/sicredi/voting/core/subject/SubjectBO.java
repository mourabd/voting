package com.sicredi.voting.core.subject;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

@Builder
@Getter
@ToString
@With
public class SubjectBO {
    private String code;
    private String title;
    private String description;
}
