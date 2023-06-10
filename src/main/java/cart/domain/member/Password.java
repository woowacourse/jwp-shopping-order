package cart.domain.member;

import cart.exception.AuthenticationException;
import cart.exception.MemberException;

import java.util.Objects;

public class Password {
    private final String value;

    public Password(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value)) {
            throw new MemberException.EmptyPassword();
        }
        if (value.length() < 4 || value.length() > 50) {
            throw new MemberException.IllegalPassword();
        }
    }

    public void checkPassword(Password other) {
        if (!Objects.equals(this, other)) {
            throw new AuthenticationException.LoginFail();
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Password{" +
                "value='" + value + '\'' +
                '}';
    }
}
