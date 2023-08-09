package com.example.userservice.service.impl;

import com.example.userservice.model.User;
import com.example.userservice.model.authorization.Response;
import com.example.userservice.model.authorization.TokenType;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.TokenService;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public Response authenticate(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        User user = userService.getByUsername(username);
        return Response.builder()
                .userId(user.getId())
                .username(username)
                .accessToken(tokenService.generate(TokenType.ACCESS, user))
                .refreshToken(tokenService.generate(TokenType.REFRESH, user))
                .build();
    }

    @Override
    public void register(User user) {
        userService.create(user);
        //TODO send activation email
    }

    @Override
    public void sendResetEmail(String email) {
        //TODO send email
    }

}
