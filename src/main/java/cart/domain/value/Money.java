package cart.domain.value;

import java.util.Objects;

public class Money {

    private final int value;

    public Money(final int value) {
        validateRange(value);
        this.value = value;
    }

    private void validateRange(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("금액은 0보다 작을 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Money money = (Money) o;
        return getValue() == money.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
