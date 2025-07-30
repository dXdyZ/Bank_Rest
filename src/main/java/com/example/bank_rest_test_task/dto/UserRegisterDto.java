package com.example.bank_rest_test_task.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegisterDto(
        @NotNull(message = "Username must be specified")
        @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters long")
        String username,
        @NotNull(message = "Password must be specified")
        @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters long")
        String password
) {
}
