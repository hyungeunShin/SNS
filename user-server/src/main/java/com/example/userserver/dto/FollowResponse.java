package com.example.userserver.dto;

import com.example.userserver.domain.Follow;

import java.time.ZonedDateTime;

public record FollowResponse(Long followId, Long userId, Long followerId, ZonedDateTime followDatetime) {
    public static FollowResponse from(Follow follow) {
        return new FollowResponse(follow.getFollowId(), follow.getUserId(), follow.getFollowerId(), follow.getFollowDatetime());
    }
}
