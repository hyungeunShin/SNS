package com.example.timelineserver.follow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowerListener {
    private final ObjectMapper objectMapper;
    private final FollowerStore followerStore;

    @KafkaListener(topics = "user.follower", groupId = "timeline-server")
    public void listen(String message) {
        try {
            FollowMessage followMessage = objectMapper.readValue(message, FollowMessage.class);
            followerStore.followUser(followMessage);
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
