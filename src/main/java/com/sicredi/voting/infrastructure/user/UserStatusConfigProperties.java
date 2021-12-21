package com.sicredi.voting.infrastructure.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "user-info-service")
public class UserStatusConfigProperties {

    @NotBlank(message = "Attribute URL is required")
    private String url;
}
