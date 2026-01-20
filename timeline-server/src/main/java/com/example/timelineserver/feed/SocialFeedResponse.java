package com.example.timelineserver.feed;

import java.time.ZonedDateTime;

public record SocialFeedResponse(Long feedId, String imageId, Long uploaderId,
                                 ZonedDateTime uploadDatetime, String contents, String uploaderName) {

}
