package com.example.userserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    private Long userId;

    private Long followerId;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime followDatetime;

    public Follow(Long userId, Long followerId) {
        this.userId = userId;
        this.followerId = followerId;
    }

    @PrePersist
    protected void onCreate() {
        followDatetime = ZonedDateTime.now();
    }
}
