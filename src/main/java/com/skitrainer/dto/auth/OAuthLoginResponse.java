package com.skitrainer.dto.auth;

public record OAuthLoginResponse(
        String token,
        String name,
        String role
) {
}
