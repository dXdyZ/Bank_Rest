package com.example.bank_rest_test_task.dto;


import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(
        @NotBlank(message = "Username must not be empty")
        String username,
        @NotBlank(message = "Password must not be empty")
        String password
) {}
