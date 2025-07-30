package com.example.bank_rest_test_task.repository;

import com.example.bank_rest_test_task.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Page<Card> findByUser_Username(String userUsername, Pageable pageable);
    Optional<Card> findByEncryptNumberAndUser_Username(String cardNumber, String userUsername);
    Optional<Card> findByEncryptNumber(String encryptNumber);
    Optional<Card> findByIdAndUser_Username(Long id, String userUsername);
}
