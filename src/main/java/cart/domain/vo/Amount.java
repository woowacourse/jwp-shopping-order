package cart.domain.vo;

import cart.exception.BusinessException;
import java.util.Objects;

public class Amount {

    private final int value;

    public Amount(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < 0) {
            throw new BusinessException("금액은 0이상이어야 합니다.");
        }
    }

    public static Amount of(final int value) {
        return new Amount(value);
    }

    public boolean isLessThan(final Amount minAmount) {
        return value < minAmount.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Amount amount = (Amount) o;
        return value == amount.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int getValue() {
        return value;
    }
}
