package cart.domain.order;

import java.util.Objects;

public class Price {
    private static final int MINIMUM_VALUE = 0;

    private final int value;

    public Price(final int value) {
        validateLessThanMinimumValue(value);
        this.value = value;
    }

    private void validateLessThanMinimumValue(final int value) {
        if (value < MINIMUM_VALUE) {
            throw new IllegalArgumentException("가격은 음수일 수 없습니다");
        }
    }

    public Price plus(final int operand) {
        return new Price(value + operand);
    }

    public Price minus(final int operand) {
        return new Price(value - operand);
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
        final Price price = (Price) o;
        return value == price.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
