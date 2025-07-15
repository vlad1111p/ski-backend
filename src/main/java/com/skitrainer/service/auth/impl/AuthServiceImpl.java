package com.skitrainer.service.auth.impl;

import com.skitrainer.dto.auth.AuthResponse;
import com.skitrainer.dto.auth.LoginRequest;
import com.skitrainer.dto.auth.RegisterRequest;
import com.skitrainer.dto.auth.SetPasswordRequest;
import com.skitrainer.model.User;
import com.skitrainer.repository.UserRepository;
import com.skitrainer.service.auth.AuthService;
import com.skitrainer.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static boolean isValidPassword(User user) {
        return user.getPassword() != null && !user.getPassword().isBlank();
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param request the registration request containing email, password, name, and role
     * @return an AuthResponse containing the JWT token, user's name, and role
     * @throws ResponseStatusException if the email is already in use or if the role is invalid
     */
    @Override
    public AuthResponse register(final RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        //TODO validate role so that there is only given roles
        final User user = User.builder()
                .id(UUID.randomUUID().toString())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.name())
                .role(User.Role.valueOf(request.role().toUpperCase()))
                .build();
        final User savedUser = userRepository.save(user);
        final String token = jwtUtil.generateToken(savedUser);

        return new AuthResponse(token, savedUser.getFullName(), savedUser.getRole().name());
    }

    /**
     * Logs in a user with the provided credentials.
     *
     * @param request the login request containing email and password
     * @return an AuthResponse containing the JWT token, user's name, and role
     * @throws ResponseStatusException if the credentials are invalid or the user cannot log in locally
     */
    @Override
    public AuthResponse login(final LoginRequest request) {
        final User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        if (!isValidPassword(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "User cannot login locally. Please use Google login.");
        }

        final String token = jwtUtil.generateToken(user);

        return new AuthResponse(token, user.getFullName(), user.getRole().name());
    }

    /**
     * Sets the password for a user.
     *
     * @param user               the user for whom the password is being set
     * @param setPasswordRequest the request containing the new password
     * @throws ResponseStatusException if the user already has a password set
     */
    @Override
    public void setPassword(final User user, final SetPasswordRequest setPasswordRequest) {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password already set");
        }
        user.setPassword(passwordEncoder.encode(setPasswordRequest.password()));
        userRepository.save(user);
    }
}
