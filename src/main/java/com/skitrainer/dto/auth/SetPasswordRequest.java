package com.skitrainer.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SetPasswordRequest(
        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#^])[A-Za-z\\d@$!%*?&.#^]{8,}$",
                message = "Password must be at least 8 characters with uppercase, lowercase, number, and special character"
        )
        String password
) {
}