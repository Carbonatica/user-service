package com.example.userservice.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetDto {

    @NotEmpty(message = "Token must be not empty.")
    private String token;

    @NotEmpty(message = "Password must be not empty.")
    private String password;

    @NotEmpty(message = "Password confirmation must be not empty.")
    private String passwordConfirmation;

}
