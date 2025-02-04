package com.example.userserver.service;

import com.example.userserver.domain.Follow;
import com.example.userserver.domain.User;
import com.example.userserver.dto.FollowMessage;
import com.example.userserver.repository.FollowRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public boolean isFollow(Long userId, Long followerId) {
        return repository.findByUserIdAndFollowerId(userId, followerId).isPresent();
    }

    @Transactional
    public Follow followUser(Long userId, Long followerId) {
        if(isFollow(userId, followerId)) {
            throw new RuntimeException();
        }

        sendFollowerMessage(userId, followerId, true);
        return repository.save(new Follow(userId, followerId));
    }

    @Transactional
    public boolean unfollowUser(Long userId, Long followerId) {
        Optional<Follow> follow = repository.findByUserIdAndFollowerId(userId, followerId);
        if(follow.isEmpty()) {
            return false;
        }

        sendFollowerMessage(userId, followerId, false);
        repository.delete(follow.get());
        return true;
    }

    private void sendFollowerMessage(Long userId, Long followerId, boolean isFollow) {
        FollowMessage message = new FollowMessage(userId, followerId, isFollow);
        try {
            kafkaTemplate.send("user.follower", objectMapper.writeValueAsString(message));
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> listFollower(Long userId) {
        return repository.findFollowersByUserId(userId);
    }

    public List<User> listFollowing(Long userId) {
        return repository.findFollowingByUserId(userId);
    }
}
