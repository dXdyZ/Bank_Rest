package com.example.bank_rest_test_task.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.time.LocalDate;

public record CardCreateDto(
        @NotNull(message = "User id must not be empty")
        @Positive(message = "Id must not be less than zero")
        Long userId,
        @NotNull(message = "Card number must be specified")
        @CreditCardNumber(message = "Incorrect card number format")
        String cardNumber,
        @NotNull(message = "Validity period of the card must be specified")
        LocalDate validityPeriod
) {
}
