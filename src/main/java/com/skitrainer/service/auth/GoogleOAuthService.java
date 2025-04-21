package com.skitrainer.service.auth;

import java.util.Map;

public interface GoogleOAuthService {
    Map<String, Object> exchangeCodeForTokens(String code);

    String buildGoogleAuthorizationUrl();
}
