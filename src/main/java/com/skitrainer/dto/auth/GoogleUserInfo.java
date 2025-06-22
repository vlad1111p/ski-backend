package com.skitrainer.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInfo(
        String id,
        String email,
        String name,
        @JsonProperty("verified_email") boolean verified
) {
}