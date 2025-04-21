package com.skitrainer.service.auth.impl;

import com.skitrainer.repository.UserRepository;
import com.skitrainer.service.auth.GoogleOAuthService;
import com.skitrainer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class GoogleOAuthServiceImpl implements GoogleOAuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public GoogleOAuthServiceImpl(
            final UserRepository userRepository,
            final JwtUtil jwtUtil,
            @Value("${google.oauth.client-id}") final String clientId,
            @Value("${google.oauth.client-secret}") final String clientSecret,
            @Value("${google.oauth.redirect-uri}") final String redirectUri

    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String buildGoogleAuthorizationUrl() {
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "https://www.googleapis.com/auth/calendar")
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .build()
                .toUriString();
    }

    @Override
    public Map<String, Object> exchangeCodeForTokens(String code) {
        return Map.of();
    }
}
