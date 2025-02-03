package com.example.notificationbatch.batch;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class NotificationInfo {
    private Long followId;
    private String email;
    private String username;
    private String followerName;
    private Long followerId;
    private ZonedDateTime followDateTime;
}