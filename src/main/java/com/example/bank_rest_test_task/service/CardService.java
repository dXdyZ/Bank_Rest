package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.dto.CardCreateDto;
import com.example.bank_rest_test_task.entity.Card;
import com.example.bank_rest_test_task.entity.StatusCard;
import com.example.bank_rest_test_task.entity.User;
import com.example.bank_rest_test_task.exception.CardNotFoundException;
import com.example.bank_rest_test_task.exception.UserNotFoundException;
import com.example.bank_rest_test_task.repository.CardRepository;
import com.example.bank_rest_test_task.util.CardFormattedService;
import com.example.bank_rest_test_task.util.CryptoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final CryptoService cryptoService;
    private final UserService userService;


    public CardService(CardRepository cardRepository, CryptoService cryptoService, UserService userService) {
        this.cardRepository = cardRepository;
        this.cryptoService = cryptoService;
        this.userService = userService;
    }

    @Transactional
    public void createCard(CardCreateDto cardCreateDto, String username) throws UserNotFoundException {
        User user = userService.findUserByUsername(username);
        Card card = Card.builder()
                .encryptNumber(cryptoService.encrypt(cardCreateDto.cardNumber()))
                .user(user)
                .validityPeriod(cardCreateDto.validityPeriod())
                .searchHash(cryptoService.calculationCardHash(cardCreateDto.cardNumber()))
                .build();
        cardRepository.save(card);
    }

    public Page<Card> getUserCardsPaginated(String username, Pageable pageable) {
        return cardRepository.findByUser_Username(username, pageable);
    }

    public Card findCardByUsernameAndCardId(Long cardId, String username) {
        return cardRepository.findByIdAndUser_Username(cardId, username).orElseThrow(
                () -> new CardNotFoundException("Card by id: %s not found for user %s".formatted(cardId, username))
        );
    }

    public Card findCardByCardNumberAndUsername(String username, String cardNumber) {
        return cardRepository.findByEncryptNumberAndUser_Username(cryptoService.calculationCardHash(cardNumber), username)
                .orElseThrow(() -> new CardNotFoundException("Card: %s not found for user %s".formatted(
                        CardFormattedService.formatedMaskedCard(cardNumber), username)));
    }

    public Card findCardByNumber(String cardNumber) {
        return cardRepository.findByEncryptNumber(cryptoService.calculationCardHash(cardNumber))
                .orElseThrow(() -> new CardNotFoundException("Card by: %s not found"
                        .formatted(CardFormattedService.formatedMaskedCard(cardNumber))));
    }

    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Transactional
    public void updateCardStatus(Long cardId, StatusCard newStatusCard) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card by id: %s not found".formatted(cardId)));
        card.setStatusCard(newStatusCard);
        cardRepository.save(card);
    }

    @Transactional
    public void deleteCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card by id: %s not found".formatted(cardId)));
        cardRepository.delete(card);
    }

    @Transactional
    public void deleteCardByCardNumber(String cardNumber) {
        Card card = cardRepository.findBySearchHash(cryptoService.calculationCardHash(cardNumber))
                .orElseThrow(() -> new CardNotFoundException("Card by id: %s not found".formatted(cardNumber)));
        cardRepository.delete(card);
    }
}









