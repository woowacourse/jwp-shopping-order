package cart.domain.member;

import cart.exception.BadRequestException;
import cart.util.Sha256Encryptor;

import static cart.exception.ErrorCode.INVALID_PASSWORD_LENGTH;

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
            throw new BadRequestException(INVALID_PASSWORD_LENGTH);
        }
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }

    public String getPassword() {
        return password;
    }
}
