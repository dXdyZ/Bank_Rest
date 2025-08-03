package com.example.bank_rest_test_task.exception;

public class CardBlockRequestNotFoundException extends RuntimeException {
    public CardBlockRequestNotFoundException(String message) {
        super(message);
    }
}
