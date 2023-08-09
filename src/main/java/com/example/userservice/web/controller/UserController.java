package com.example.userservice.web.controller;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import com.example.userservice.web.dto.UserDto;
import com.example.userservice.web.dto.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserDto> getAll(@RequestParam int page,
                                @RequestParam int size) {
        List<User> users = userService.getAll(page, size);
        return userMapper.toDto(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("canAccessUser(#id)")
    public UserDto getById(@PathVariable UUID id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @GetMapping("/@{username}")
    @PreAuthorize("canAccessUser(#username)")
    public UserDto getByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("canAccessUser(#id)")
    public void deleteById(@PathVariable UUID id) {
        userService.delete(id);
    }

}
