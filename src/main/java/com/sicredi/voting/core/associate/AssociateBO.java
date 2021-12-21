package com.sicredi.voting.core.associate;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class AssociateBO {
    private final String firstName;
    private final String lastName;
    private final String cpf;
}
