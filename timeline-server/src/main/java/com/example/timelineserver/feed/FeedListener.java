package com.example.timelineserver.feed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedListener {
    private final ObjectMapper objectMapper;
    private final FeedStore feedStore;

    @KafkaListener(topics = "feed.created", groupId = "timeline-server")
    public void listen(String message) {
        try {
            SocialFeedResponse feed = objectMapper.readValue(message, SocialFeedResponse.class);
            feedStore.savePost(feed);
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
