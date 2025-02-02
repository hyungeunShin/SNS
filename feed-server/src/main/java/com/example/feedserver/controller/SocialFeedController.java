package com.example.feedserver.controller;

import com.example.feedserver.dto.SocialFeedCreateRequest;
import com.example.feedserver.dto.SocialFeedResponse;
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
        return service.getAllFeeds().stream().map(SocialFeedResponse::from).toList();
    }

    @GetMapping("/user/{userId}")
    public List<SocialFeedResponse> getAllFeedsByUser(@PathVariable("userId") Long userId) {
        return service.getAllFeedsByUploaderId(userId).stream().map(SocialFeedResponse::from).toList();
    }

    @GetMapping("/{id}")
    public SocialFeedResponse getFeedById(@PathVariable("id") Long feedId) {
        return SocialFeedResponse.from(service.getFeedById(feedId));
    }

    @PostMapping
    public SocialFeedResponse createFeed(@RequestBody SocialFeedCreateRequest dto) {
        return SocialFeedResponse.from(service.createFeed(dto.imageId(), dto.uploaderId(), dto.contents()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeed(@PathVariable("id") Long feedId) {
        service.deleteFeed(feedId);
        return ResponseEntity.ok().build();
    }
}
