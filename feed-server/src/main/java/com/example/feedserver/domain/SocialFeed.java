package com.example.feedserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;

    private String imageId;

    private Long uploaderId;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime uploadDatetime;

    private String contents;

    @PrePersist
    protected void onCreate() {
        this.uploadDatetime = ZonedDateTime.now();
    }

    public SocialFeed(String imageId, Long uploaderId, String contents) {
        this.imageId = imageId;
        this.uploaderId = uploaderId;
        this.contents = contents;
    }
}
