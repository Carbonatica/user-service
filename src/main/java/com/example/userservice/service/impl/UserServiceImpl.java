package com.example.userservice.service.impl;

import com.example.userservice.model.User;
import com.example.userservice.model.authorization.PasswordReset;
import com.example.userservice.model.exception.ResourceAlreadyExistsException;
import com.example.userservice.model.exception.ResourceNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<User> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable)
                .getContent();
    }

    @Override
    public void activate(UUID id) {
        User user = getById(id);
        if (user.getStatus() != User.Status.NOT_ACTIVATED) {
            throw new IllegalStateException("User is already activated.");
        }
        user.setStatus(User.Status.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public void resetPassword(UUID id, PasswordReset dto) {
        //TODO send email
    }

    @Override
    public void create(User user) {
        if (!Objects.equals(user.getPassword(), user.getPasswordConfirmation())) {
            throw new IllegalArgumentException("Пароли должны совпадать.");
        }
        Optional<User> withSameUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> withSamePhoneNumber = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (withSameUsername.isPresent() || withSamePhoneNumber.isPresent()) {
            throw new ResourceAlreadyExistsException();
        }
        Set<User.Role> userRoles = Set.of(User.Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordConfirmation(null);
        user.setRoles(userRoles);
        user.setLastSeen(LocalDateTime.now());
        user.setStatus(User.Status.NOT_ACTIVATED);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        //TODO send email
        User user = getById(id);
        user.setStatus(User.Status.DELETED);
        userRepository.save(user);
    }

}
