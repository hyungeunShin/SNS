package com.example.feedserver.service;

import com.example.feedserver.domain.SocialFeed;
import com.example.feedserver.dto.SocialFeedResponse;
import com.example.feedserver.dto.UserResponse;
import com.example.feedserver.repository.SocialFeedRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SocialFeedService {
    private final SocialFeedRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final RestClient restClient = RestClient.create();

    public List<SocialFeed> getAllFeeds() {
        return repository.findAll();
    }

    public List<SocialFeed> getAllFeedsByUploaderId(Long uploaderId) {
        return repository.findByUploaderId(uploaderId);
    }

    public SocialFeed getFeedById(Long feedId) {
        return repository.findById(feedId).orElseThrow(NullPointerException::new);
    }

    @Transactional
    public SocialFeed createFeed(String imageId, Long uploaderId, String contents) {
        SocialFeed socialFeed = repository.save(new SocialFeed(imageId, uploaderId, contents));

        UserResponse uploader = getUser(uploaderId);
        SocialFeedResponse response = SocialFeedResponse.from(socialFeed, uploader.username());
        try {
            kafkaTemplate.send("feed.created", objectMapper.writeValueAsString(response));
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return socialFeed;
    }

    public void refreshAllFeeds() {
        List<SocialFeed> feeds = getAllFeeds();
        feeds.forEach(feed -> {
            UserResponse uploader = getUser(feed.getUploaderId());
            SocialFeedResponse response = SocialFeedResponse.from(feed, uploader.username());
            try {
                kafkaTemplate.send("feed.created", objectMapper.writeValueAsString(response));
            } catch(JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Transactional
    public void deleteFeed(Long feedId) {
        repository.deleteById(feedId);
    }

    public UserResponse getUser(Long userId) {
        return restClient.get()
                         .uri("http://localhost:8081/api/users/" + userId)
                         .retrieve()
                         .onStatus(HttpStatusCode::isError, (request, response) -> {
                             throw new RuntimeException("invalid server response " + response.getStatusText());
                         })
                         .body(UserResponse.class);
    }
}
