package com.example.userservice.service.props;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String secret;
    private Integer access;
    private Integer refresh;
    private Integer activation;
    private Integer reset;

}
