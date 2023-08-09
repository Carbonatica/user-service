package com.example.userservice.model.authorization;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordReset {

    private String token;
    private String password;
    private String passwordConfirmation;

}
