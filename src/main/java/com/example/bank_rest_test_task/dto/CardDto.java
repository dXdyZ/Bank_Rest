package com.example.bank_rest_test_task.dto;

import com.example.bank_rest_test_task.entity.StatusCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Long cardId;
    private String cardNumber;
    private LocalDate validityPeriod;
    private StatusCard statusCard;
    private BigDecimal balance;
}
