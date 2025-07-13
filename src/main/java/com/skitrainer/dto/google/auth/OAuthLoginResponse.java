package com.skitrainer.dto.google.auth;

public record OAuthLoginResponse(
        String token,
        String name,
        String role
) {
}
