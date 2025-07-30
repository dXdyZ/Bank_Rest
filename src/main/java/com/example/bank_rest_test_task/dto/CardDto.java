package com.example.bank_rest_test_task.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardDto(
        Long cardId,
        String cardNumber,
        LocalDate validityPeriod,
        String statusCard,
        BigDecimal balance
) {
}
