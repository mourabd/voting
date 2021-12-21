package com.sicredi.voting.api.subject;

import com.sicredi.voting.core.subject.SubjectBO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SubjectApiMapper {

    public static SubjectBO toBusinessObject(SubjectRequestDTO subjectRequestDTO) {
        return SubjectBO.builder()
            .code(subjectRequestDTO.getCode())
            .title(subjectRequestDTO.getTitle())
            .description(subjectRequestDTO.getDescription())
            .build();
    }

    public static SubjectResponseDTO toDataTransferObject(SubjectBO subjectBO) {
        return SubjectResponseDTO.builder()
            .code(subjectBO.getCode())
            .title(subjectBO.getTitle())
            .description(subjectBO.getDescription())
            .build();
    }
}
