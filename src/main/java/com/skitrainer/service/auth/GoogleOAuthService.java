package com.skitrainer.service.auth;

import com.skitrainer.dto.auth.OAuthLoginResponse;

public interface GoogleOAuthService {
    OAuthLoginResponse exchangeCodeForTokens(String code);

    String buildGoogleAuthorizationUrl();
}
