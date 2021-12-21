package com.sicredi.voting.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    static final long serialVersionUID = -7034897190745766939L;

    public BadRequestException(String message) {
        super(message);
        log.error(message);
    }
}
