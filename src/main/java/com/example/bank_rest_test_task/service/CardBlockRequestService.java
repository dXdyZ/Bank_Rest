package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.dto.CreateCardBlockRequestDto;
import com.example.bank_rest_test_task.entity.*;
import com.example.bank_rest_test_task.exception.BlockRequestNotFoundException;
import com.example.bank_rest_test_task.exception.CardBlockRequestNotFoundException;
import com.example.bank_rest_test_task.exception.CardBlockedException;
import com.example.bank_rest_test_task.exception.CardNotFoundException;
import com.example.bank_rest_test_task.repository.CardBlockRequestRepository;
import com.example.bank_rest_test_task.util.CryptoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class CardBlockRequestService {
    private final CardBlockRequestRepository cardBlockRequestRepository;
    private final CryptoService cryptoService;
    private final UserService userService;
    private final CardService cardService;

    public CardBlockRequestService(CardBlockRequestRepository cardBlockRequestRepository, CryptoService cryptoService,
                                   UserService userService, CardService cardService) {
        this.cardBlockRequestRepository = cardBlockRequestRepository;
        this.cryptoService = cryptoService;
        this.userService = userService;
        this.cardService = cardService;
    }

    @Transactional
    public void createRequest(CreateCardBlockRequestDto createCardBlockRequestDto, String username)
            throws CardNotFoundException {

        Card card = cardService.findCardByUsernameAndCardId(createCardBlockRequestDto.cardId(), username);

        if (card.getStatusCard() == StatusCard.BLOCKED) {
            throw new CardBlockedException("You cannot block an inactive card");
        }

        cardBlockRequestRepository.save(
                CardBlockRequest.builder()
                        .card(card)
                        .requester(card.getUser())
                        .reason(createCardBlockRequestDto.reason())
                        .build()
        );

        cardService.updateCardStatus(createCardBlockRequestDto.cardId(), StatusCard.PENDING_BLOCKED);
    }

    @Transactional
    public void processRequest(Long requestId, BlockRequestStatus status, String adminUsername) {
        CardBlockRequest request = cardBlockRequestRepository.findById(requestId).orElseThrow(
                () -> new BlockRequestNotFoundException("Request to block by id: %s not found".formatted(requestId)));
        User admin = userService.findUserByUsername(adminUsername);

        if (status == BlockRequestStatus.APPROVED) {
            cardService.updateCardStatus(request.getCard().getId(), StatusCard.BLOCKED);
        }
        if (status == BlockRequestStatus.REJECTED) {
            cardService.updateCardStatus(request.getCard().getId(), StatusCard.ACTIVE);
        }

        request.setProcessedBy(admin);
        request.setBlockRequestStatus(status);
        request.setProcessedAt(OffsetDateTime.now());

        cardBlockRequestRepository.save(request);
    }

    @Transactional
    public Page<CardBlockRequest> findFilterCardBlockRequest(BlockRequestStatus blockRequestStatus, Long requesterId,
                                                             String cardNumber, OffsetDateTime createStartDate,
                                                             OffsetDateTime createEndDate,
                                                             Pageable pageable) {
        Page<CardBlockRequest> result;
        if (!cardNumber.isBlank()) {
            result = cardBlockRequestRepository.findFilterCardBlockRequest(blockRequestStatus, requesterId,
                    cryptoService.calculationCardHash(cardNumber), createStartDate, createEndDate, pageable);
        } else {
            result = cardBlockRequestRepository.findFilterCardBlockRequest(blockRequestStatus, requesterId,
                    null, createStartDate, createEndDate, pageable);
        }
        return result;
    }

    public CardBlockRequest findCardBlockRequestById(Long id) {
        return cardBlockRequestRepository.findById(id).orElseThrow(
                () -> new CardBlockRequestNotFoundException("Request for blocking by id: %s not found".formatted(id)));
    }

    public Page<CardBlockRequest> findCardBlockRequestByProcessed(Long processedId, Pageable pageable) {
        return cardBlockRequestRepository.findAllByProcessedBy_Id(processedId, pageable);
    }
}
