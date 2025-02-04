package com.example.timelineserver.follow;

public record FollowMessage(Long userId, Long followerId, boolean isFollow) {

}
