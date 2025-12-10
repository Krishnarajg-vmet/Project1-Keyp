package com.kay.keyp.service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final String secretKey;

    public EncryptionService(@Value("${ENCRYPTION_SECRET_KEY}") String secretKey) {
        this.secretKey = secretKey;
    }

    private SecretKeySpec getSecretKeySpec(String myKey) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = myKey.getBytes("UTF-8");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // 128-bit AES key
        return new SecretKeySpec(key, "AES");
    }

    public String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = getSecretKeySpec(secretKey);

            // Generate IV
            byte[] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));

            // Prepend IV to the encrypted content
            byte[] ivAndEncrypted = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, ivAndEncrypted, 0, iv.length);
            System.arraycopy(encrypted, 0, ivAndEncrypted, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(ivAndEncrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }

    public String decrypt(String strToDecrypt) {
        try {
            byte[] ivAndEncrypted = Base64.getDecoder().decode(strToDecrypt);

            // Extract IV and encrypted data
            byte[] iv = Arrays.copyOfRange(ivAndEncrypted, 0, 16);
            byte[] encrypted = Arrays.copyOfRange(ivAndEncrypted, 16, ivAndEncrypted.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = getSecretKeySpec(secretKey);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password", e);
        }
    }
}
