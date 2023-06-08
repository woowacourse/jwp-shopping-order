package cart.domain.member;

import cart.exception.MemberException;

import java.util.Objects;

public class Email {
    public static final int MAX_LENGTH = 50;
    public static final int MIN_LENGTH = 0;
    private final String value;

    public Email(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value)) {
            throw new MemberException.EmptyEmail();
        }
        if (value.length() <= MIN_LENGTH || value.length() >= MAX_LENGTH) {
            throw new MemberException.IllegalEmail();
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

    @Override
    public String toString() {
        return "Email{" +
                "value='" + value + '\'' +
                '}';
    }
}
