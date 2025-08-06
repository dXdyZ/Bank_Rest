package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.dto.JwtTokenDto;
import com.example.bank_rest_test_task.exception.AuthenticationFailedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public JwtTokenDto loing(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );
            return tokenService.getTokens(authentication.getName());
        } catch (DisabledException e) {
            throw new AuthenticationFailedException("Account is disable");
        } catch (LockedException e) {
            throw new AuthenticationFailedException("Account is locked");
        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Invalid username or password");
        }
    }
}
