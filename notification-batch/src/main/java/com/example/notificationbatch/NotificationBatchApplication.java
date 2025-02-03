package com.example.notificationbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NotificationBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationBatchApplication.class, args);
    }
}
