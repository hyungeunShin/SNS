package com.example.userserver.dto;

import com.example.userserver.domain.User;

public record UserResponse(
        Long userId, String username, String email
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getUserId(), user.getUsername(), user.getEmail());
    }
}
