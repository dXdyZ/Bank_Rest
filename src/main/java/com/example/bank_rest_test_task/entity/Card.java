package com.example.bank_rest_test_task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Builder
@Table(name = "cards")
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", unique = true)
    private String encryptNumber;

    @Column(name = "validity_period", nullable = false)
    private LocalDate validityPeriod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_card", nullable = false)
    private StatusCard statusCard;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "search_hash")
    private String searchHash;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
