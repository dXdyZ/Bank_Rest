package com.example.bank_rest_test_task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Entity
@Builder
@Table(name = "card_block_requests")
@NoArgsConstructor
@AllArgsConstructor
public class CardBlockRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester;

    @Column(name = "reason")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    @Column(name = "block_request_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BlockRequestStatus blockRequestStatus = BlockRequestStatus.APPROVED;

    @Builder.Default
    @Column(name = "create_at")
    private OffsetDateTime createAt = OffsetDateTime.now();
}










