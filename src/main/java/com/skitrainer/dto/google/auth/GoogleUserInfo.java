package com.skitrainer.dto.google.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInfo(
        String id,
        String email,
        @JsonProperty("verified_email") boolean verified,
        String name,
        String picture
) {
}