package com.example.userserver.dto;

public record UserSignUpRequest(String username, String email, String plainPassword) {
}
