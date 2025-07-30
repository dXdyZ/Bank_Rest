package com.example.bank_rest_test_task.util;

public class CardFormattedService {
    private static final String MASK  = "**** **** **** ";
    private static final int VISIBLE_DIGITS = 4;

    public static String formatedMaskedCard(String cardNumber) {
        return MASK + cardNumber.substring(cardNumber.length() - VISIBLE_DIGITS);
    }
}
