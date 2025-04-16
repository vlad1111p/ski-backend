package com.skitrainer.service;

import com.skitrainer.dto.auth.AuthResponse;
import com.skitrainer.dto.auth.LoginRequest;
import com.skitrainer.dto.auth.OAuthLoginRequest;
import com.skitrainer.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse oauthLogin(OAuthLoginRequest request);
}
