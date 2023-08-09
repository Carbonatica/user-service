package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.model.authorization.PasswordReset;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User getById(UUID id);

    User getByUsername(String username);

    List<User> getAll(int page, int size);

    void activate(UUID id);

    void resetPassword(UUID id, PasswordReset dto);

    void create(User user);

    void delete(UUID id);

}
