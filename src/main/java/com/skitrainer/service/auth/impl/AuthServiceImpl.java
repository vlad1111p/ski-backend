package com.skitrainer.service.auth.impl;

import com.skitrainer.dto.auth.AuthResponse;
import com.skitrainer.dto.auth.LoginRequest;
import com.skitrainer.dto.auth.RegisterRequest;
import com.skitrainer.model.User;
import com.skitrainer.repository.UserRepository;
import com.skitrainer.service.auth.AuthService;
import com.skitrainer.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(final RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        final User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .role(User.Role.valueOf(request.role().toUpperCase()))
                .build();
        final User savedUser = userRepository.save(user);
        final String token = jwtUtil.generateToken(savedUser);

        return new AuthResponse(token, savedUser.getName(), savedUser.getRole().name());
    }

    @Override
    public AuthResponse login(final LoginRequest request) {
        final User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        final String token = jwtUtil.generateToken(user);

        return new AuthResponse(token, user.getName(), user.getRole().name());
    }
}
