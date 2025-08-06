package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.dto.PaymentDto;
import com.example.bank_rest_test_task.entity.Card;
import com.example.bank_rest_test_task.entity.StatusCard;
import com.example.bank_rest_test_task.entity.TransferHistory;
import com.example.bank_rest_test_task.exception.CardBlockedException;
import com.example.bank_rest_test_task.exception.InsufficientFundsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {
    private final TransferHistoryService transferHistoryService;
    private final CardService cardService;

    public PaymentService(TransferHistoryService transferHistoryService, CardService cardService) {
        this.transferHistoryService = transferHistoryService;
        this.cardService = cardService;
    }

    @Transactional
    public List<Card> transferMoney(PaymentDto paymentDto, Long userId) {
        Card fromCard = cardService.findCardByUserIdAndCardId(paymentDto.fromCardId(), userId);
        Card toCard = cardService.findCardByUserIdAndCardId(paymentDto.toCardId(), userId);

        chekCard(fromCard, toCard);

        if (fromCard.getBalance().compareTo(paymentDto.amount()) <= 0) {
            throw new InsufficientFundsException("There are not enough funds on the card");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(paymentDto.amount()));
        toCard.setBalance(toCard.getBalance().add(paymentDto.amount()));

        transferHistoryService.saveTransferHistory(TransferHistory.builder()
                        .fromCard(fromCard)
                        .toCard(toCard)
                        .amount(paymentDto.amount())
                        .user(fromCard.getUser())
                        .comment(paymentDto.comment())
                .build());
        cardService.saveCard(fromCard);
        cardService.saveCard(toCard);
        return List.of(fromCard, toCard);
    }

    private void chekCard(Card fromCard, Card toCard) {
        if (fromCard.getStatusCard().equals(StatusCard.BLOCKED)){
            throw new CardBlockedException("Card by id: %s is blocked for operation".formatted(fromCard.getId()));
        }
        if (toCard.getStatusCard().equals(StatusCard.BLOCKED)) {
            throw new CardBlockedException("Card by id: %s is blocked for operation".formatted(toCard.getId()));
        }
        if (fromCard.getStatusCard().equals(StatusCard.EXPIRED)) {
            throw new CardBlockedException("Card by id: %s has expired".formatted(fromCard.getId()));
        }
        if (toCard.getStatusCard().equals(StatusCard.EXPIRED)) {
            throw new CardBlockedException("Card by id: %s has expired".formatted(toCard.getId()));
        }
    }
}
