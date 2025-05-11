package com.skitrainer.service.auth;

import com.skitrainer.dto.auth.AuthResponse;
import com.skitrainer.dto.auth.LoginRequest;
import com.skitrainer.dto.auth.RegisterRequest;
import com.skitrainer.dto.auth.SetPasswordRequest;
import com.skitrainer.model.User;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void setPassword(User user, SetPasswordRequest password);
}
