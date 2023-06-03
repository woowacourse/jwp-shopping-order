package shop.util;

import shop.exception.EncryptionException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
    private static final String ENCRYPTION_ALGORITHM = "SHA-256";

    public static String encrypt(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
            messageDigest.update(text.getBytes());

            return bytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException(e.getMessage());
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();

        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }

}
