package com.example.feedserver.dto;

public record SocialFeedCreateRequest(String imageId, Long uploaderId, String contents) {
}
