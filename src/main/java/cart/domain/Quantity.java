package cart.domain;

import java.util.Objects;

class Quantity {

    private static final int MINIMUM_QUANTITY = 1;

    private int value;

    public Quantity(final int quantity) {
        validateQuantity(quantity);
        this.value = quantity;
    }

    private void validateQuantity(final int quantity) {
        if (quantity < MINIMUM_QUANTITY) {
            throw new IllegalArgumentException("변경 가능한 최소 수량은 " + MINIMUM_QUANTITY + "개 입니다.");
        }
    }

    public int getValue() {
        return value;
    }

    public void updateQuantity(final int quantity) {
        validateQuantity(quantity);
        this.value = quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
