package com.example.bank_rest_test_task.dto;

public record CreateCardBlockRequestDto(
        Long cardId,
        String reason
) {}
