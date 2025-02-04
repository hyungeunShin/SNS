package com.example.timelineserver.feed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FeedStore {
    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    public void savePost(SocialFeedResponse feed) {
        try {
            redis.opsForZSet().add("feed:" + feed.uploaderId(), objectMapper.writeValueAsString(feed), feed.uploadDatetime().toEpochSecond());
            redis.opsForZSet().add("feed:all", objectMapper.writeValueAsString(feed), feed.uploadDatetime().toEpochSecond());
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SocialFeedResponse> allFeed() {
        Set<String> savedFeed = redis.opsForZSet().reverseRange("feed:all", 0, -1);
        return savedFeed.stream().map(feed -> {
            try {
                return objectMapper.readValue(feed, SocialFeedResponse.class);
            } catch(JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<SocialFeedResponse> listFeed(String userId) {
        Set<String> savedFeed = redis.opsForZSet().reverseRange("feed:" + userId, 0, -1);
        return savedFeed.stream().map(feed -> {
            try {
                return objectMapper.readValue(feed, SocialFeedResponse.class);
            } catch(JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public Long likePost(Long userId, Long postId) {
        return redis.opsForSet().add("likes:" + postId, String.valueOf(userId));
    }

    public Long unlikePost(Long userId, Long postId) {
        return redis.opsForSet().remove("likes:" + postId, String.valueOf(userId));
    }

    public Boolean isLikePost(Long userId, Long postId) {
        return redis.opsForSet().isMember("likes:" + postId, String.valueOf(userId));
    }

    public Long countLikes(Long postId) {
        return redis.opsForSet().size("likes:" + postId);
    }

    public Map<Long, Long> countLikes(List<Long> postIds) {
        Map<Long, Long> likesMap = new HashMap<>();

        List<Object> results = redis.executePipelined((RedisCallback<Object>) connection -> {
            StringRedisConnection stringRedisConn = (StringRedisConnection) connection;

            for(Long postId : postIds) {
                stringRedisConn.sCard("likes:" + postId);
            }

            return null;
        });

        int index = 0;
        for(Long postId : postIds) {
            Long likeCount = (Long) results.get(index++);
            likesMap.put(postId, likeCount);
        }

        return likesMap;
    }
}
