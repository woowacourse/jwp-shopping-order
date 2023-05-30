package cart.domain.member;

import java.util.Objects;

class Email {

    private static final String EMAIL_REGULAR_EXPRESSION = "[a-z0-9]+@[a-z]+\\.[a-z]{2,3}";

    private final String value;

    public Email(final String email) {
        validateEmail(email);
        this.value = email;
    }

    private void validateEmail(final String email) {
        if (!email.matches(EMAIL_REGULAR_EXPRESSION)) {
            throw new IllegalArgumentException("이메일 형식이 유효하지 않습니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
