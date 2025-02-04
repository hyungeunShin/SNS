package com.example.timelineserver.follow;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class FollowerStore {
    private final StringRedisTemplate redis;

    public void followUser(FollowMessage followMessage) {
        if(followMessage.isFollow()) {
            redis.opsForSet().add("user:follower:" + followMessage.followerId(), String.valueOf(followMessage.userId()));
        } else {
            redis.opsForSet().remove("user:follower:" + followMessage.followerId(), String.valueOf(followMessage.userId()));
        }
    }

    public Set<String> listFollower(String userId) {
        return redis.opsForSet().members("user:follower:" + userId);
    }
}
