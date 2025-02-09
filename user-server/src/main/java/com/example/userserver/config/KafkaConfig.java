package com.example.userserver.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic feedTopic() {
        return new NewTopic("user.follower", 1, (short) 1);
    }
}
