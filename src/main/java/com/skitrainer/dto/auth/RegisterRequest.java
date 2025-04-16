package com.skitrainer.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,

        @Size(min = 6, message = "Password must be at least 6 characters")
        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Role is required (TRAINER or PARTICIPANT)")
        String role) {
}
