package com.example.userservice.model.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoTokenException extends RuntimeException {

    public NoTokenException(String message) {
        super(message);
    }

}
