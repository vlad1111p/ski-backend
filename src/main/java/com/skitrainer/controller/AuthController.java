package com.skitrainer.controller;

import com.skitrainer.dto.auth.LoginRequest;
import com.skitrainer.dto.auth.RegisterRequest;
import com.skitrainer.dto.auth.SetPasswordRequest;
import com.skitrainer.model.User;
import com.skitrainer.service.auth.AuthService;
import com.skitrainer.service.auth.GoogleOAuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final GoogleOAuthService googleOAuthService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody final RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody final LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/set-password")
    public ResponseEntity<?> setPassword(@AuthenticationPrincipal User user,
                                         @Valid @RequestBody SetPasswordRequest request) {
        authService.setPassword(user, request);
        return ResponseEntity.ok(Map.of("message", "Password set successfully"));
    }

    @GetMapping("/google-authorize")
    public void authorizeWithGoogle(final HttpServletResponse response) throws IOException {
        response.sendRedirect(googleOAuthService.buildGoogleAuthorizationUrl());
    }

    @GetMapping("/google-callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam final String code) {
        return ResponseEntity.ok(googleOAuthService.exchangeCodeForTokens(code));
    }
}
