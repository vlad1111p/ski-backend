package com.skitrainer.repository;

import com.skitrainer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(String userId);

    Optional<User> findByGoogleId(String googleId);

    boolean existsByGoogleId(String googleId);

    boolean existsByEmail(String email);
}
