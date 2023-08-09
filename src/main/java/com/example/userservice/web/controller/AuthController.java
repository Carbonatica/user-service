package com.example.userservice.web.controller;

import com.example.userservice.model.User;
import com.example.userservice.model.authorization.PasswordReset;
import com.example.userservice.model.authorization.Request;
import com.example.userservice.model.authorization.Response;
import com.example.userservice.model.authorization.TokenType;
import com.example.userservice.model.exception.TokenNotValidException;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.TokenService;
import com.example.userservice.service.UserService;
import com.example.userservice.web.dto.PasswordResetDto;
import com.example.userservice.web.dto.UserDto;
import com.example.userservice.web.dto.mapper.PasswordResetMapper;
import com.example.userservice.web.dto.mapper.UserMapper;
import com.example.userservice.web.dto.validation.OnCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final TokenService tokenService;

    private final UserMapper userMapper;
    private final PasswordResetMapper passwordResetMapper;

    @PostMapping("/login")
    public Response login(@RequestBody @Validated Request request) {
        return authService.authenticate(request.getUsername(),
                request.getPassword());
    }

    @PostMapping("/register")
    public void register(@RequestBody @Validated(OnCreate.class) UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        authService.register(user);
    }

    @PostMapping("/activate")
    public void activate(@RequestBody String token) {
        if (!tokenService.validate(token, TokenType.ACTIVATION)) {
            throw new TokenNotValidException("Token is expired.");
        }
        UUID userId = tokenService.getUserId(token);
        userService.activate(userId);
    }

    @PostMapping("/password/restore")
    public void restorePassword(@Validated @RequestBody PasswordResetDto dto) {
        if (!tokenService.validate(dto.getToken(), TokenType.RESTORE)) {
            throw new TokenNotValidException("Token is expired.");
        }
        UUID userId = tokenService.getUserId(dto.getToken());
        PasswordReset passwordReset = passwordResetMapper.toEntity(dto);
        userService.resetPassword(userId, passwordReset);
    }

    @PostMapping("/forget")
    public void sendResetEmail(@RequestBody String email) {
        authService.sendResetEmail(email);
    }

    @PostMapping("/refresh")
    public Response refresh(@RequestBody String refreshToken) {
        return tokenService.refresh(refreshToken);
    }

}
