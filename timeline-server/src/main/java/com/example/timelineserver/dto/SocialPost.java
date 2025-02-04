package com.example.timelineserver.dto;

import com.example.timelineserver.feed.SocialFeedResponse;

import java.time.ZonedDateTime;

public record SocialPost(
        Long feedId, String imageId, String uploaderName, Long uploaderId,
        ZonedDateTime uploadDatetime, String contents, Long likes
) {
    public static SocialPost from(SocialFeedResponse response, Long likes) {
        return new SocialPost(response.feedId(), response.imageId(), response.uploaderName(),
                response.uploaderId(), response.uploadDatetime(), response.contents(), likes == null ? 0 : likes);
    }
}
