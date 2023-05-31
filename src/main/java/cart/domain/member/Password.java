package cart.domain.member;

import java.util.Objects;

public class Password {

    private static final int MIN_PASSWORD_LENGTH = 4;
    private final String password;

    public Password(final String password) {
        validatePassword(password);
        this.password = password;
    }

    private void validatePassword(final String password) {
        validateBlank(password);
        validateLength(password);
    }

    private void validateBlank(final String password) {
        if (password.isBlank()) {
            throw new IllegalArgumentException("Password 는 공백일 수 없습니다.");
        }
    }

    private void validateLength(final String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password 는 "+MIN_PASSWORD_LENGTH+"자 이상이어야 합니다.");
        }
    }

    public String password() {
        return password;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
