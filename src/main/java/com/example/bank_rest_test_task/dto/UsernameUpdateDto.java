package com.example.bank_rest_test_task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsernameUpdateDto(
        @NotBlank(message = "New username must not be empty")
        @Size(min = 3, max = 50, message = "Username must be in the range of 3 to 50 characters")
        String newUsername
) {
}
