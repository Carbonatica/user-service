package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.model.authorization.Response;

public interface AuthService {

    Response authenticate(String username, String password);

    void register(User user);

    void sendResetEmail(String email);

}
