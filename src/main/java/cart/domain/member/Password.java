package cart.domain.member;

import cart.util.Sha256Encryptor;

public class Password {

    private final String password;

    private Password(final String password) {
        this.password = password;
    }

    public static Password encrypt(final String plainPassword) {
        validatePlainPassword(plainPassword);
        return new Password(Sha256Encryptor.encrypt(plainPassword));
    }

    public static Password of(final String encryptedPassword) {
        return new Password(encryptedPassword);
    }

    private static void validatePlainPassword(final String password) {
        if (password.length() < 4 || password.length() > 10) {
            throw new IllegalArgumentException("비밀번호는 4글자 이상 10글자 이하로 입력해주세요.");
        }
    }

    public String getPassword() {
        return password;
    }
}
