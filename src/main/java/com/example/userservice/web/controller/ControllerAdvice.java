package com.example.userservice.web.controller;

import com.example.userservice.model.exception.ResourceAlreadyExistsException;
import com.example.userservice.model.exception.ResourceNotFoundException;
import com.example.userservice.web.dto.MessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDto illegalArgumentException(IllegalArgumentException e) {
        return new MessageDto(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageDto resourceNotFoundException(ResourceNotFoundException e) {
        return new MessageDto("User not found.");
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public MessageDto resourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        return new MessageDto("User already exists.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    public MessageDto handleBadCredentialsException(RuntimeException e) {
        return new MessageDto("Incorrect username or password.");
    }

    @ExceptionHandler
    public MessageDto exception(Exception e) {
        return new MessageDto("Internal error.");
    }

}
