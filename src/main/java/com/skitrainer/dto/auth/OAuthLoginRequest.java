package com.skitrainer.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record OAuthLoginRequest(
        @NotBlank(message = "Google ID Token is required")
        String idToken) {
}
