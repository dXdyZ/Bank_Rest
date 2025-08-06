package com.example.bank_rest_test_task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateCardBlockRequestDto(
        @Positive(message = "Id must not be less than zero")
        Long cardId,
        @NotBlank(message = "Reason should not be empty")
        @Size(min = 5, max = 255, message = "Reason description should be in the range of 5 to 255 characters")
        String reason
) {}
