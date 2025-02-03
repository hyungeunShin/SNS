package com.example.userserver.controller;

import com.example.userserver.dto.FollowRequest;
import com.example.userserver.dto.FollowResponse;
import com.example.userserver.dto.UserResponse;
import com.example.userserver.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService service;

    @GetMapping("/followers/{userId}")
    public List<UserResponse> listFollowers(@PathVariable("userId") Long userId) {
        return service.listFollower(userId).stream().map(UserResponse::from).toList();
    }

    @GetMapping("/followings/{userId}")
    public List<UserResponse> listFollowings(@PathVariable("userId") Long userId) {
        return service.listFollowing(userId).stream().map(UserResponse::from).toList();
    }

    @GetMapping("/follow/{userId}/{followerId}")
    public boolean isFollow(@PathVariable("userId") Long userId, @PathVariable("followerId") Long followerId) {
        return service.isFollow(userId, followerId);
    }

    @PostMapping("/follow")
    public FollowResponse followUser(@RequestBody FollowRequest dto) {
        return FollowResponse.from(service.followUser(dto.userId(), dto.followerId()));
    }

    @PostMapping("/unfollow")
    public Boolean unfollowUser(@RequestBody FollowRequest dto) {
        return service.unfollowUser(dto.userId(), dto.followerId());
    }
}
