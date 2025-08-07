package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.dto.CardCreateDto;
import com.example.bank_rest_test_task.entity.Card;
import com.example.bank_rest_test_task.entity.StatusCard;
import com.example.bank_rest_test_task.entity.User;
import com.example.bank_rest_test_task.exception.CardDuplicateException;
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
    public void createCard(CardCreateDto cardCreateDto) throws UserNotFoundException {
        User user = userService.findUserById(cardCreateDto.userId());
        if (cardRepository.existsBySearchHash(cryptoService.calculationCardHash(cardCreateDto.cardNumber()))) {
            throw new CardDuplicateException("Card by number: %s already exists".formatted(cardCreateDto.cardNumber()));
        }
        Card card = Card.builder()
                .encryptNumber(cryptoService.encrypt(cardCreateDto.cardNumber()))
                .user(user)
                .validityPeriod(cardCreateDto.validityPeriod())
                .searchHash(cryptoService.calculationCardHash(cardCreateDto.cardNumber()))
                .statusCard(StatusCard.ACTIVE)
                .build();
        cardRepository.save(card);
    }

    public Boolean existsCardByNumber(String cardNumber) {
        return cardRepository.existsBySearchHash(cryptoService.calculationCardHash(cardNumber));
    }

    public Page<Card> getUserCardsPaginated(Long userId, Pageable pageable) {
        return cardRepository.findByUser_Id(userId, pageable);
    }

    public Card findCardByUserIdAndCardId(Long cardId, Long userId) {
        return cardRepository.findByIdAndUser_id(cardId, userId).orElseThrow(
                () -> new CardNotFoundException("Card by id: %s not found for user %s".formatted(cardId, userId))
        );
    }

    public Card findCardByCardNumberAndUserId(Long userId, String cardNumber) {
        return cardRepository.findByEncryptNumberAndUser_Id(cryptoService.calculationCardHash(cardNumber), userId)
                .orElseThrow(() -> new CardNotFoundException("Card: %s not found for user %s".formatted(
                        CardFormattedService.formatedMaskedCard(cardNumber), userId)));
    }

    public Card findCardByNumber(String cardNumber) {
        return cardRepository.findBySearchHash(cryptoService.calculationCardHash(cardNumber))
                .orElseThrow(() -> new CardNotFoundException("Card by: %s not found"
                        .formatted(CardFormattedService.formatedMaskedCard(cardNumber))));
    }

    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    public Card findCardById(Long id) {
        return cardRepository.findById(id).orElseThrow(
                () -> new CardNotFoundException("Card by id: %s not found".formatted(id)));
    }

    @Transactional
    public Card updateCardStatus(Long cardId, StatusCard newStatusCard) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card by id: %s not found".formatted(cardId)));
        card.setStatusCard(newStatusCard);
        return cardRepository.save(card);
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

    public Page<Card> findCardsByUsername(String username, Pageable pageable) {
        return cardRepository.findByUser_Username(username, pageable);
    }

    public Page<Card> findAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }
}









