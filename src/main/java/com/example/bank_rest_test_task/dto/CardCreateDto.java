package com.example.bank_rest_test_task.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.time.LocalDate;

public record CardCreateDto(
        @NotNull(message = "Card number must be specified")
        @CreditCardNumber(message = "Incorrect card number format")
        String cardNumber,
        @NotNull(message = "Validity period of the card must be specified")
        LocalDate validityPeriod
) {
}
