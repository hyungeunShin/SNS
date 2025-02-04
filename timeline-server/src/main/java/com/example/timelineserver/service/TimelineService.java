package com.example.timelineserver.service;

import com.example.timelineserver.dto.SocialPost;
import com.example.timelineserver.feed.FeedStore;
import com.example.timelineserver.feed.SocialFeedResponse;
import com.example.timelineserver.follow.FollowerStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TimelineService {
    private final FeedStore feedStore;
    private final FollowerStore followerStore;

    public List<SocialPost> listUserFeed(String userId) {
        List<SocialFeedResponse> feedList = feedStore.listFeed(userId);
        Map<Long, Long> likes = feedStore.countLikes(feedList.stream().map(SocialFeedResponse::feedId).toList());
        return feedList.stream()
                       .map(post -> SocialPost.from(post, likes.getOrDefault(post.feedId(), 0L)))
                       .toList();
    }

    public List<SocialPost> getRandomPost(String userId, double randomPost) {
        List<SocialPost> myPost = userId.equals("none") ? List.of() : listMyFeed(userId);
        int randomPostSize = Math.max(10, (int) Math.ceil(myPost.size() * randomPost));
        List<SocialPost> allPost = new ArrayList<>(listAllFeed());

        Set<Long> myPostIds = myPost.stream()
                                    .map(SocialPost::feedId)
                                    .collect(Collectors.toSet());

        allPost.removeIf(post -> myPostIds.contains(post.feedId()));

        List<SocialPost> picked;
        if(randomPostSize >= allPost.size()) {
            picked = allPost;
        } else {
            Collections.shuffle(allPost);
            picked = new ArrayList<>(allPost.subList(0, randomPostSize));
        }

        allPost.removeIf(post -> myPostIds.contains(post.feedId()));

        return Stream.concat(myPost.stream(), picked.stream())
                     .sorted(Comparator.comparing(SocialPost::uploadDatetime).reversed())
                     .collect(Collectors.toList());
    }

    public List<SocialPost> listFollowerFeed(Set<String> followerSet) {
        return followerSet.stream()
                          .map(this::listUserFeed)
                          .filter(Objects::nonNull)
                          .flatMap(List::stream)
                          .collect(Collectors.toList());
    }

    public List<SocialPost> listMyFeed(String userId) {
        Set<String> followers = followerStore.listFollower(String.valueOf(userId));
        List<SocialPost> myPost = listUserFeed(userId);
        List<SocialPost> followerFeed = listFollowerFeed(followers);

        return Stream.concat(myPost.stream(), followerFeed.stream())
                     .sorted(Comparator.comparing(SocialPost::uploadDatetime).reversed())
                     .collect(Collectors.toList());
    }

    public List<SocialPost> listAllFeed() {
        List<SocialFeedResponse> feedList = feedStore.allFeed();
        Map<Long, Long> likes = feedStore.countLikes(feedList.stream().map(SocialFeedResponse::feedId).toList());
        return feedList.stream()
                       .map(post -> SocialPost.from(post, likes.getOrDefault(post.feedId(), 0L)))
                       .toList();
    }

    public boolean likePost(Long userId, Long postId) {
        if(feedStore.isLikePost(userId, postId)) {
            feedStore.unlikePost(userId, postId);
            return false;
        } else {
            feedStore.likePost(userId, postId);
            return true;
        }
    }

    public Long countLike(Long postId) {
        return feedStore.countLikes(postId);
    }
}
