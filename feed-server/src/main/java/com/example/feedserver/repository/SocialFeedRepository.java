package com.example.feedserver.repository;

import com.example.feedserver.domain.SocialFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocialFeedRepository extends JpaRepository<SocialFeed, Long> {
    List<SocialFeed> findByUploaderId(Long uploaderId);
}
