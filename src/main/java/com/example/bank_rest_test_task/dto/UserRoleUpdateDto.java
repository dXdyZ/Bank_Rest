package com.example.bank_rest_test_task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRoleUpdateDto(
        @NotBlank(message = "Role name must not be empty")
        @Pattern(regexp = "USER|ADMIN", message = "Role must be USER or ADMIN")
        String role
) {}
