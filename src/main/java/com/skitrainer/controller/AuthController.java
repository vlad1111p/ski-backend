package com.skitrainer.controller;

import com.skitrainer.dto.auth.LoginRequest;
import com.skitrainer.dto.auth.RegisterRequest;
import com.skitrainer.service.auth.AuthService;
import com.skitrainer.service.auth.GoogleOAuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @GetMapping("/google-authorize")
    public void authorizeWithGoogle(final HttpServletResponse response) throws IOException {
        response.sendRedirect(googleOAuthService.buildGoogleAuthorizationUrl());
    }

    @GetMapping("/google-callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam final String code) {
        return ResponseEntity.ok(googleOAuthService.exchangeCodeForTokens(code));
    }
}
