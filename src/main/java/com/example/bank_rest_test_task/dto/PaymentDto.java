package com.example.bank_rest_test_task.dto;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentDto(
        @NotNull(message = "Card from which the funds are being withdrawn must be indicated")
        Long fromCardId,
        @NotNull(message = "Card to which the funds are credited must be indicated")
        Long toCardId,
        @Digits(integer = 15, fraction = 4)
        @Positive(message = "Amount must not be less than zero")
        BigDecimal amount,
        String comment
) {
}
