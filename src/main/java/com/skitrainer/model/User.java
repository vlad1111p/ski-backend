package com.skitrainer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String fullName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(nullable = false)
    private boolean googleAuthenticated = false;
    @Column(unique = true)
    private String googleId;
    @Column(length = 2048)
    private String googleAccessToken;
    @Column(length = 2048)
    private String googleRefreshToken;
    private String googlePictureUrl;
    private Instant tokenExpiry;

    @PrePersist
    public void assignId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public enum Role {
        TRAINER,
        PARTICIPANT
    }
}
