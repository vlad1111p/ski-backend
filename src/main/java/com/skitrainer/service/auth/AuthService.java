package com.skitrainer.service.auth;

import com.skitrainer.dto.auth.AuthResponse;
import com.skitrainer.dto.auth.LoginRequest;
import com.skitrainer.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
