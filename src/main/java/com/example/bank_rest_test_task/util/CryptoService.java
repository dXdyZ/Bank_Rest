package com.example.bank_rest_test_task.util;

import jakarta.annotation.PostConstruct;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class CryptoService {
    private final StringEncryptor encryptor;
    private final String hashKey;

    public CryptoService(StringEncryptor encryptor, @Value("${payment.card.tokenization.hash-key}") String hashKey) {
        this.encryptor = encryptor;
        this.hashKey = hashKey;
    }

    public String encrypt(String rawData) {
        return encryptor.encrypt(rawData);
    }

    public String decrypt(String encryptData) {
        return encryptor.decrypt(encryptData);
    }

    public String calculationCardHash(String number) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            hmac.init(new SecretKeySpec(hashKey.getBytes(), "HmacSHA256"));
            byte[] hash = hmac.doFinal(number.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("Invalid HMAC key", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
