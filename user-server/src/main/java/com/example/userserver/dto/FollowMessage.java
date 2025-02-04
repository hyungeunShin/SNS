package com.example.userserver.dto;

public record FollowMessage(Long userId, Long followerId, boolean isFollow) {

}
