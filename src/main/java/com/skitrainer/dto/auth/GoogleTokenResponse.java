package com.skitrainer.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("expires_in") int expiresIn,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("scope") String scope
) {
}
