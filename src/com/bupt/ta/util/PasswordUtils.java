package com.bupt.ta.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码哈希工具。
 */
public final class PasswordUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    private PasswordUtils() {
    }

    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String rawPassword, String encodedSalt) {
        if (rawPassword == null || encodedSalt == null) {
            throw new IllegalArgumentException("Password and salt must not be null");
        }

        byte[] salt = Base64.getDecoder().decode(encodedSalt);
        PBEKeySpec keySpec = new PBEKeySpec(rawPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(keySpec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Password hashing is not available", ex);
        } finally {
            keySpec.clearPassword();
        }
    }

    public static boolean matches(String rawPassword, String encodedSalt, String expectedHash) {
        if (rawPassword == null || encodedSalt == null || expectedHash == null) {
            return false;
        }
        return expectedHash.equals(hashPassword(rawPassword, encodedSalt));
    }
}
