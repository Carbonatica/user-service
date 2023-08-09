package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.model.authorization.Response;
import com.example.userservice.model.authorization.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface TokenService {

    String generate(TokenType type, User user);

    Response refresh(String refreshToken);

    Authentication getAuthentication(String token);

    boolean validate(String token, TokenType tokenType);

    String resolve(HttpServletRequest request);

    UUID getUserId(String token);

}
