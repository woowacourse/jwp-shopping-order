package cart.domain.Member;

import java.util.Objects;

public class Password {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private final String password;

    public Password(final String password) {
        this.password = password;
    }

    public static Password from(String password) {
        validatePassword(password);
        return new Password(password);
    }

    private static Password validatePassword(final String password) {
        validateBlank(password);
        validateLength(password);
        return new Password(password);
    }

    private static void validateBlank(final String password) {
        if (password.isBlank()) {
            throw new IllegalArgumentException("Password 는 공백일 수 없습니다.");
        }

        if(password.contains(" ")){
            throw new IllegalArgumentException("Password 는 공백을 포함할 수 없습니다.");
        }
    }

    private static void validateLength(final String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password 는 6자 이상이어야 합니다.");
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
