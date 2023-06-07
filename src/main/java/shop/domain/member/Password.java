package shop.domain.member;

import shop.exception.ShoppingException;
import shop.util.Encryptor;

public class Password {
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int MAX_PASSWORD_LENGTH = 10;

    private final String password;

    private Password(String password) {
        this.password = password;
    }

    public static Password createFromNaturalPassword(String password) {
        validate(password);
        String encryptedPassword = Encryptor.encrypt(password);

        return new Password(encryptedPassword);
    }

    private static void validate(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new ShoppingException("비밀번호는 4글자 이상, 10글자 이하여야 합니다.");
        }
    }

    public static Password createFromEncryptedPassword(String password) {
        return new Password(password);
    }

    public String getPassword() {
        return password;
    }
}
