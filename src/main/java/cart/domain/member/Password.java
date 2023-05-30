package cart.domain.member;

import org.springframework.util.StringUtils;

import java.util.Objects;

class Password {

    private final String value;

    public Password(final String password) {
        validatePassword(password);
        this.value = password;
    }

    private void validatePassword(final String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("비밀번호는 비어있을 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }

    public boolean checkPassword(final String password) {
        return value.equals(password);
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
}
