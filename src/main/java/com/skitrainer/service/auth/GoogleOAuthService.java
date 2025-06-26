package com.skitrainer.service.auth;


public interface GoogleOAuthService {
    String exchangeCodeForTokens(String code);

    String buildGoogleAuthorizationUrl();
}
