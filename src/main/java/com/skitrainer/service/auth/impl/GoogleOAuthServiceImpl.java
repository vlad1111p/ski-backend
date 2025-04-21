package com.skitrainer.service.auth.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.skitrainer.dto.auth.GoogleUserInfo;
import com.skitrainer.dto.auth.OAuthLoginResponse;
import com.skitrainer.model.User;
import com.skitrainer.repository.UserRepository;
import com.skitrainer.service.auth.GoogleOAuthService;
import com.skitrainer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    public OAuthLoginResponse exchangeCodeForTokens(final String code) {
        try {
            final GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    "https://oauth2.googleapis.com/token",
                    clientId,
                    clientSecret,
                    code,
                    redirectUri
            ).execute();

            final String accessToken = tokenResponse.getAccessToken();
            final String refreshToken = tokenResponse.getRefreshToken();

            if (accessToken == null) {
                throw new RuntimeException("Access token not received from Google");
            }

            final GoogleUserInfo userInfo = fetchGoogleUserInfo(accessToken);

            final User user = userRepository.findByEmail(userInfo.email())
                    .orElseGet(() -> userRepository.save(
                            User.builder()
                                    .email(userInfo.email())
                                    .name(userInfo.name())
                                    .role(User.Role.PARTICIPANT)
                                    // Optional: store refreshToken/accessToken here
                                    .build()
                    ));

            final String jwt = jwtUtil.generateToken(user);

            return new OAuthLoginResponse(
                    jwt,
                    user.getName(),
                    user.getRole().name());

        } catch (Exception e) {
            throw new RuntimeException("Failed to authenticate with Google", e);
        }
    }

    //switch to Google People API to get user more info including profile picture, phone number, etc.
    private GoogleUserInfo fetchGoogleUserInfo(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        final HttpEntity<Void> request = new HttpEntity<>(headers);
        final RestTemplate restTemplate = new RestTemplate();

        final ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                request,
                GoogleUserInfo.class
        );

        final GoogleUserInfo userInfo = response.getBody();
        if (userInfo == null || userInfo.email() == null) {
            throw new RuntimeException("Failed to fetch user info from Google");
        }

        return userInfo;
    }
}
