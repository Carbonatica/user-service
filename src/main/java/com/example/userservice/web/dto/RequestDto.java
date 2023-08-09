package com.example.userservice.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestDto {

    @NotEmpty(message = "Username must be not empty.")
    private String username;

    @NotEmpty(message = "Password must be not empty.")
    private String password;

}
