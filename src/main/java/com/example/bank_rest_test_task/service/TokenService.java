package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.dto.JwtTokenDto;
import com.example.bank_rest_test_task.security.jwt.JwtUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public TokenService(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    public JwtTokenDto getTokens(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        JwtTokenDto tokens = JwtTokenDto.builder()
                .accessToken(jwtUtils.generationAccessToken(userDetails))
                .refreshToken(jwtUtils.generationRefreshToken(userDetails))
                .build();

        return tokens;
    }
}
