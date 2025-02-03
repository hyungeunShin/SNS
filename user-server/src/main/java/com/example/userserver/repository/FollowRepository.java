package com.example.userserver.repository;

import com.example.userserver.domain.Follow;
import com.example.userserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByUserIdAndFollowerId(Long userId, Long followerId);

    @Query("SELECT u FROM Follow f JOIN User u ON(u.userId = f.followerId) WHERE f.userId = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM Follow f JOIN User u ON(u.userId = f.userId) WHERE f.followerId = :userId")
    List<User> findFollowingByUserId(@Param("userId") Long userId);
}
