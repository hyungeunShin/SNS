package com.example.userserver.controller;

import com.example.userserver.dto.UserResponse;
import com.example.userserver.dto.UserSignInRequest;
import com.example.userserver.dto.UserSignUpRequest;
import com.example.userserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public UserResponse signUpUser(@RequestBody UserSignUpRequest dto) {
        return UserResponse.from(service.signUp(dto.username(), dto.email(), dto.plainPassword()));
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable("id") Long id) {
        return UserResponse.from(service.getUser(id));
    }

    @GetMapping("/username/{username}")
    public UserResponse getUserByUsername(@PathVariable("username") String username) {
        return UserResponse.from(service.getUserByUsername(username));
    }

    @PostMapping("/signIn")
    public UserResponse signIn(@RequestBody UserSignInRequest dto) {
        return UserResponse.from(service.signIn(dto.username(), dto.plainPassword()));
    }
}
