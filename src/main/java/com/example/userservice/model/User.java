package com.example.userservice.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Country country;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    private String password;

    @Transient
    private String passwordConfirmation;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime created;
    private LocalDateTime updated;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    public enum Role {

        ROLE_USER,
        ROLE_ADMIN

    }

    public enum Country {

        BELARUS

    }

    public enum Status {

        ACTIVE,
        NOT_ACTIVATED,
        BLOCKED,
        DELETED

    }

}
