package shop.domain.member;

import shop.exception.ShoppingException;

public class NaturalPassword implements Password {
    private static final int MIN_PASSWORD_LENGTH = 4;
    private static final int MAX_PASSWORD_LENGTH = 10;

    private final String password;

    public NaturalPassword(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new ShoppingException("비밀번호는 4글자 이상, 10글자 이하여야 합니다.");
        }
    }

    public String getPassword() {
        return password;
    }
}
