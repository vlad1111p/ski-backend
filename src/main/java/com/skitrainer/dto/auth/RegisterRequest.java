package com.skitrainer.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#^])[A-Za-z\\d@$!%*?&.#^]{8,}$",
                message = "Password must be at least 8 characters, include uppercase, lowercase, number, and special character"
        )
        String password,

        @NotBlank(message = "Role is required (TRAINER or PARTICIPANT)")
        String role) {
}
