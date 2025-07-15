package com.skitrainer.service.auth;


import com.skitrainer.model.User;

public interface GoogleOAuthService {
    String exchangeCodeForTokens(String code);

    String buildGoogleAuthorizationUrl();

    void refreshAccessToken(User user);
}
