package com.example.userserver.service;

import com.example.userserver.domain.Follow;
import com.example.userserver.domain.User;
import com.example.userserver.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository repository;

    public boolean isFollow(Long userId, Long followerId) {
        return repository.findByUserIdAndFollowerId(userId, followerId).isPresent();
    }

    @Transactional
    public Follow followUser(Long userId, Long followerId) {
        if(isFollow(userId, followerId)) {
            throw new RuntimeException();
        }

        return repository.save(new Follow(userId, followerId));
    }

    @Transactional
    public boolean unfollowUser(Long userId, Long followerId) {
        Optional<Follow> follow = repository.findByUserIdAndFollowerId(userId, followerId);
        if(follow.isEmpty()) {
            return false;
        }

        repository.delete(follow.get());
        return true;
    }

    public List<User> listFollower(Long userId) {
        return repository.findFollowersByUserId(userId);
    }

    public List<User> listFollowing(Long userId) {
        return repository.findFollowingByUserId(userId);
    }
}
