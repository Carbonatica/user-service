package com.example.userservice.web.dto;

import com.example.userservice.model.User;
import com.example.userservice.web.dto.validation.OnCreate;
import com.example.userservice.web.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDto {

    @NotNull(message = "Id cannot be null",
            groups = {OnUpdate.class})
    private UUID id;

    @NotNull(message = "First name cannot be null",
            groups = {OnUpdate.class, OnCreate.class})
    private String firstName;

    @NotNull(message = "Last name cannot be null",
            groups = {OnUpdate.class, OnCreate.class})
    private String lastName;

    @NotNull(message = "Username cannot be null",
            groups = {OnUpdate.class, OnCreate.class})
    private String username;

    @NotNull(message = "Phone number cannot be null",
            groups = {OnUpdate.class, OnCreate.class})
    private String phoneNumber;

    @NotNull(message = "Country cannot be null",
            groups = {OnUpdate.class, OnCreate.class})
    private User.Country country;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<User.Role> roles;

    @NotNull(message = "Password cannot be null",
            groups = {OnUpdate.class, OnCreate.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "Password confirmation cannot be null",
            groups = {OnCreate.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordConfirmation;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User.Status status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastSeen;

}
