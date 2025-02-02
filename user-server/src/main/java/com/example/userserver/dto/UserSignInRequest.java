package com.example.userserver.dto;

public record UserSignInRequest(String username, String plainPassword) {
}
