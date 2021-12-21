package com.sicredi.voting.core.session;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

@Getter
@Builder
@ToString
@With
public class SessionResultBO {

    private final SessionBO session;
    private final SessionResultBO.ResultBO result;

    @Getter
    @Builder
    @ToString
    public static class ResultBO {
        private final long yes;
        private final long no;
    }
}
