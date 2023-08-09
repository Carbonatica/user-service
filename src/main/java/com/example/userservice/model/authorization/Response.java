package com.example.userservice.model.authorization;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
public class Response {

    private UUID userId;
    private String username;
    private String accessToken;
    private String refreshToken;

}
