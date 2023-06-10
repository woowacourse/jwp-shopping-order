package cart.domain.member;

import cart.exception.MemberException;

import java.util.Objects;

public class Nickname {
    public static final int MIN_LENGTH = 0;
    public static final int MAX_LENGTH = 20;

    private final String value;

    public Nickname(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (Objects.isNull(value)) {
            throw new MemberException.EmptyNickname();
        }
        if (value.length() == MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new MemberException.IllegalNickname();
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Nickname nickname = (Nickname) o;
        return Objects.equals(value, nickname.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Nickname{" +
                "value='" + value + '\'' +
                '}';
    }
}
