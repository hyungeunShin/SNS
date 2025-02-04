package com.example.feedserver.controller;

import com.example.feedserver.domain.SocialFeed;
import com.example.feedserver.dto.SocialFeedCreateRequest;
import com.example.feedserver.dto.SocialFeedResponse;
import com.example.feedserver.dto.UserResponse;
import com.example.feedserver.service.SocialFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class SocialFeedController {
    private final SocialFeedService service;

    @GetMapping
    public List<SocialFeedResponse> getAllFeeds() {
        return service.getAllFeeds()
                      .stream()
                      .map(socialFeed -> SocialFeedResponse.from(socialFeed, service.getUser(socialFeed.getUploaderId()).username()))
                      .toList();
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh() {
        service.refreshAllFeeds();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public List<SocialFeedResponse> getAllFeedsByUser(@PathVariable("userId") Long userId) {
        return service.getAllFeedsByUploaderId(userId)
                      .stream()
                      .map(socialFeed -> SocialFeedResponse.from(socialFeed, service.getUser(socialFeed.getUploaderId()).username()))
                      .toList();
    }

    @GetMapping("/{id}")
    public SocialFeedResponse getFeedById(@PathVariable("id") Long feedId) {
        SocialFeed feed = service.getFeedById(feedId);
        return SocialFeedResponse.from(feed, service.getUser(feed.getUploaderId()).username());
    }

    @PostMapping
    public SocialFeedResponse createFeed(@RequestBody SocialFeedCreateRequest dto) {
        UserResponse user = service.getUser(dto.uploaderId());
        return SocialFeedResponse.from(service.createFeed(dto.imageId(), dto.uploaderId(), dto.contents()), user.username());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeed(@PathVariable("id") Long feedId) {
        service.deleteFeed(feedId);
        return ResponseEntity.ok().build();
    }
}
