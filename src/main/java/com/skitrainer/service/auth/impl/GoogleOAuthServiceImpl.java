package com.skitrainer.service.auth.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.skitrainer.client.GoogleUserInfoClient;
import com.skitrainer.dto.google.auth.GoogleUserInfo;
import com.skitrainer.model.User;
import com.skitrainer.repository.UserRepository;
import com.skitrainer.service.auth.GoogleOAuthService;
import com.skitrainer.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class GoogleOAuthServiceImpl implements GoogleOAuthService {

    private final UserRepository userRepository;
    private final GoogleUserInfoClient googleUserInfoClient;
    private final JwtUtil jwtUtil;

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public GoogleOAuthServiceImpl(
            final UserRepository userRepository,
            final GoogleUserInfoClient googleUserInfoClient,
            final JwtUtil jwtUtil,
            @Value("${google.oauth.client-id}") final String clientId,
            @Value("${google.oauth.client-secret}") final String clientSecret,
            @Value("${google.oauth.redirect-uri}") final String redirectUri

    ) {
        this.googleUserInfoClient = googleUserInfoClient;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    private static void updateFromGoogle(final User user,
                                         final GoogleUserInfo userInfo,
                                         final GoogleTokenResponse googleTokenResponse) {
        user.setEmail(userInfo.email());
        user.setFullName(userInfo.name());
        user.setGoogleAuthenticated(true);
        user.setGoogleAccessToken(googleTokenResponse.getAccessToken());
        user.setGoogleRefreshToken(googleTokenResponse.getRefreshToken());
        user.setTokenExpiry(Instant.now().plusSeconds(googleTokenResponse.getExpiresInSeconds()));
        user.setGooglePictureUrl(userInfo.picture());

        if (userInfo.id() != null) {
            user.setGoogleId(userInfo.id());
        }

        if (user.getRole() == null) {
            user.setRole(User.Role.PARTICIPANT);
        }
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
    }

    /**
     * Builds the Google authorization URL for OAuth 2.0 authentication.
     *
     * @return the Google authorization URL as a String
     */
    @Override
    public String buildGoogleAuthorizationUrl() {
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(" ",
                        "https://www.googleapis.com/auth/userinfo.email",
                        "https://www.googleapis.com/auth/userinfo.profile",
                        "https://www.googleapis.com/auth/calendar"
                )).queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .build()
                .toUriString();
    }

    /**
     * Exchanges the authorization code for access and refresh tokens.
     *
     * @param code the authorization code received from Google
     * @return a JWT token for the authenticated user
     * @throws ResponseStatusException if there is an error during the token exchange or user info retrieval
     */
    @Override
    public String exchangeCodeForTokens(final String code) {
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


            final String bearerToken = "Bearer " + tokenResponse.getAccessToken();
            final GoogleUserInfo userInfo = googleUserInfoClient.getUserInfo(bearerToken);

            final User user = userRepository.findByEmail(userInfo.email())
                    .orElse(new User());

            updateFromGoogle(user, userInfo, tokenResponse);

            return jwtUtil.generateToken(userRepository.save(user));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to authenticate with Google", e);
        }
    }

    /**
     * Refreshes the access token for a user using their refresh token.
     *
     * @param user the user whose access token is to be refreshed
     * @throws ResponseStatusException if there is an error during the token refresh
     */
    @Override
    public void refreshAccessToken(final User user) {
        try {
            final GoogleTokenResponse response = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    user.getGoogleRefreshToken(),
                    clientId,
                    clientSecret
            ).execute();

            user.setGoogleAccessToken(response.getAccessToken());
            user.setTokenExpiry(Instant.now().plusSeconds(response.getExpiresInSeconds()));
            userRepository.save(user);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to refresh access token", e);
        }
    }
}
