package com.example.bank_rest_test_task.controller;

import com.example.bank_rest_test_task.dto.CreateCardBlockRequestDto;
import com.example.bank_rest_test_task.service.CardBlockRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blocks")
public class CardBlockController {
    private final CardBlockRequestService blockService;

    public CardBlockController(CardBlockRequestService blockService) {
        this.blockService = blockService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBlockRequest(@AuthenticationPrincipal Jwt jwt,
                                   @Valid @RequestBody CreateCardBlockRequestDto cardBlockDto) {
        Long userId = Long.valueOf(jwt.getSubject());

        blockService.createBlockRequest(cardBlockDto, userId);
    }
}
