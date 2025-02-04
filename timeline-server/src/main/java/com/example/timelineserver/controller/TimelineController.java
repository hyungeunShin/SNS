package com.example.timelineserver.controller;

import com.example.timelineserver.dto.LikeResponse;
import com.example.timelineserver.dto.SocialPost;
import com.example.timelineserver.service.TimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timeline")
@RequiredArgsConstructor
public class TimelineController {
    private final TimelineService timelineService;

    @GetMapping
    public List<SocialPost> listAllFeed() {
        return timelineService.listAllFeed();
    }

    @GetMapping("/{userId}")
    public List<SocialPost> listFeed(@PathVariable("userId") String userId,
                                     @RequestParam(value = "followingFeed", required = false) boolean includeFollowingFeed,
                                     @RequestParam(value = "randomPost", required = false, defaultValue = "0") double randomPost) {
        if(randomPost > 0) {
            return timelineService.getRandomPost(userId, randomPost);
        }

        return includeFollowingFeed ? timelineService.listMyFeed(userId) : timelineService.listUserFeed(userId);
    }

    @GetMapping("/like/{postId}/{userId}")
    public LikeResponse likePost(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
        boolean isLike = timelineService.likePost(userId, postId);
        Long count = timelineService.countLike(postId);
        return new LikeResponse(count, isLike);
    }
}
