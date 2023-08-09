package com.example.userservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class MessageDto {

    private String message;

    private Map<String, String> errors;

    public MessageDto(String message) {
        this.message = message;
    }

}
