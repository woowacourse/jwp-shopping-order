package cart.domain.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Service {
    public static String encrypt(final String target) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(target.getBytes());
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException ignored) {
            throw new RuntimeException("암호화 중 오류가 발생하였습니다.");
        }
    }

    private static String bytesToHex(byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
