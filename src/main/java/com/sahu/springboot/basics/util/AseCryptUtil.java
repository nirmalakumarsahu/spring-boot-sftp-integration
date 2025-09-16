package com.sahu.springboot.basics.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@UtilityClass
public class AseCryptUtil {

    private final String ALGORITHM = "AES/GCM/NoPadding";
    private final String ALGORITHM_INSTANCE = "AES";
    private final int AES_KEY_SIZE = 256;
    private final int GCM_IV_LENGTH = 12;
    private final int GCM_TAG_LENGTH = 16;

    public String generateSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_INSTANCE);
            keyGenerator.init(AES_KEY_SIZE);
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            log.error("Error generating secret key: {}", e.getMessage(), e);
            return null;
        }
    }

    private SecretKey generateStringToSecretKey(String keyString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, ALGORITHM_INSTANCE);
    }

    public String encrypt(String data, String secretKey) {
        if (Objects.isNull(data) || data.isEmpty() || Objects.isNull(secretKey)) {
            log.debug("Invalid input for encryption");
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);

            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, generateStringToSecretKey(secretKey), gcmSpec);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes());

            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            log.error("Error during encryption: {}", e.getMessage());
            return null;
        }
    }

    public String decrypt(String encryptedData, String secretKey) {
        if (Objects.isNull(encryptedData) || encryptedData.isEmpty() || Objects.isNull(secretKey)) {
            log.debug("Invalid input for decryption");
            return null;
        }

        try {
            byte[] combined = Base64.getDecoder().decode(encryptedData);

            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, iv.length);

            // Extract encrypted data
            byte[] encryptedBytes = new byte[combined.length - iv.length];
            System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

            // Initialize cipher in decryption mode
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, generateStringToSecretKey(secretKey), gcmSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes);
        } catch (Exception e) {
            log.error("Error during decryption: {}", e.getMessage());
            return null;
        }
    }

}