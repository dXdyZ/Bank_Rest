package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.dto.JwtTokenDto;
import com.example.bank_rest_test_task.exception.AuthenticationFailedException;
import com.example.bank_rest_test_task.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.bank_rest_test_task.security.CustomUserDetailsService;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenService {
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    public TokenService(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    public JwtTokenDto getTokens(String username) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        JwtTokenDto tokens = JwtTokenDto.builder()
                .accessToken(jwtUtils.generationAccessToken(userDetails))
                .refreshToken(jwtUtils.generationRefreshToken(userDetails))
                .build();

        return tokens;
    }

    public JwtTokenDto refreshAccessToken(String refreshToken) {
        log.info("REFRESH TOKE: {}", refreshToken);
        if (!jwtUtils.validationToken(refreshToken)) {
            log.error("TOKEN IS NOT VALID: {}", refreshToken);
            throw new AuthenticationFailedException("Refresh token is invalid or expired. Please log in again");
        }
        UserDetails userDetails = customUserDetailsService.loadUserById(Long.valueOf(jwtUtils.extractUserId(refreshToken)));
        return JwtTokenDto.builder()
                .accessToken(jwtUtils.generationAccessToken(userDetails))
                .build();
    }
}
