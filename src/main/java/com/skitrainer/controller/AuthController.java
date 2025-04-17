package com.skitrainer.controller;

import com.skitrainer.dto.auth.LoginRequest;
import com.skitrainer.dto.auth.OAuthLoginRequest;
import com.skitrainer.dto.auth.RegisterRequest;
import com.skitrainer.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/oauth-login")
    public ResponseEntity<?> oauthLogin(@Valid @RequestBody OAuthLoginRequest request) {
        return ResponseEntity.ok(authService.oauthLogin(request));
    }
}
