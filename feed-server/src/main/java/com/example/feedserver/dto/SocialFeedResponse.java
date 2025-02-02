package com.example.feedserver.dto;

import com.example.feedserver.domain.SocialFeed;

import java.time.ZonedDateTime;

public record SocialFeedResponse(
        Long feedId, String imageId, Long uploaderId, ZonedDateTime uploadDatetime, String contents
) {
    public static SocialFeedResponse from(SocialFeed socialFeed) {
        return new SocialFeedResponse(socialFeed.getFeedId(), socialFeed.getImageId(), socialFeed.getUploaderId(), socialFeed.getUploadDatetime(), socialFeed.getContents());
    }
}
