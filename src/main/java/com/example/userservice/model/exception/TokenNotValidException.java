package com.example.userservice.model.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TokenNotValidException extends RuntimeException {

    public TokenNotValidException(String message) {
        super(message);
    }

}
