package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.dto.PaymentDto;
import com.example.bank_rest_test_task.entity.Card;
import com.example.bank_rest_test_task.entity.StatusCard;
import com.example.bank_rest_test_task.exception.CardBlockedException;
import com.example.bank_rest_test_task.exception.InsufficientFundsException;
import com.example.bank_rest_test_task.exception.InvalidAmountException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    private final TransferHistoryService transferHistoryService;
    private final CardService cardService;

    public PaymentService(TransferHistoryService transferHistoryService, CardService cardService) {
        this.transferHistoryService = transferHistoryService;
        this.cardService = cardService;
    }

    public void transferMoney(PaymentDto paymentDto, String username) {
        Card fromCard = cardService.findCardByUsernameAndCardId(paymentDto.fromCardId(), username);
        Card toCard = cardService.findCardByUsernameAndCardId(paymentDto.toCardId(), username);

        chekCard(fromCard, toCard);

        if (fromCard.getBalance().compareTo(paymentDto.amount()) <= 0) {
            throw new InsufficientFundsException("There are not enough funds on the card");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(paymentDto.amount()));
        toCard.setBalance(toCard.getBalance().add(paymentDto.amount()));


    }

    private void chekCard(Card fromCard, Card toCard) {
        if (fromCard.getStatusCard().equals(StatusCard.BLOCKED)){
            throw new CardBlockedException("Card: %s is blocked for operation".formatted(fromCard));
        }
        if (toCard.getStatusCard().equals(StatusCard.BLOCKED)) {
            throw new CardBlockedException("Card: %s is blocked for operation".formatted(toCard));
        }
        if (fromCard.getStatusCard().equals(StatusCard.EXPIRED)) {
            throw new CardBlockedException("Card: %s has expired".formatted(fromCard));
        }
        if (toCard.getStatusCard().equals(StatusCard.EXPIRED)) {
            throw new CardBlockedException("Card: %s has expired".formatted(toCard));
        }
    }
}
