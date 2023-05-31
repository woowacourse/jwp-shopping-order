package cart.domain;

import cart.exception.ErrorMessage;
import cart.exception.OrderException;
import java.util.Objects;

public class DeliveryFee {

    private static final int DEFAULT_FEE = 3000;
    public static final DeliveryFee DEFAULT = new DeliveryFee(DEFAULT_FEE);
    private static final int FREE_FEE = 0;
    public static final DeliveryFee FREE = new DeliveryFee(FREE_FEE);
    private final int value;

    public DeliveryFee(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 0) {
            throw new OrderException(ErrorMessage.INVALID_DELIVERY_FEE);
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
        DeliveryFee that = (DeliveryFee) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
