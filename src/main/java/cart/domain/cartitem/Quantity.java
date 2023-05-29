package cart.domain.cartitem;

import java.util.Objects;

public class Quantity {

    private final int quantity;

    public Quantity(final int quantity) {
        validatePositive(quantity);
        this.quantity = quantity;
    }

    private void validatePositive(int target) {
        if (target <= 0) {
            throw new IllegalArgumentException("수량은 양수여야합니다.");
        }
    }

    public Quantity changeQuantity(int quantity) {
        return new Quantity(quantity);
    }

    public Quantity addQuantity(int quantity) {
        return new Quantity(this.quantity + quantity);
    }

    public int getQuantity() {
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
