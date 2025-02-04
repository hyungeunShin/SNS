package com.example.feedserver.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic feedTopic() {
        return new NewTopic("feed.created", 1, (short) 1);
    }
}
