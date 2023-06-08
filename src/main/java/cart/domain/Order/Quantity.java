package cart.domain.Order;

import java.util.Objects;

public class Quantity {
    private final int quantity;

    public Quantity(int quantity) {
        this.quantity = quantity;
    }

    public static Quantity from(int quantity) {
        validateQuantity(quantity);
        return new Quantity(quantity);
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 0보다 작을 수 없습니다.");
        }
    }

    public int quantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity1 = (Quantity) o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
