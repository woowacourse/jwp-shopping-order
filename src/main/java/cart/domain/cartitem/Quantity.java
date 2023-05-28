package cart.domain.cartitem;

import java.util.Objects;

public class Quantity {

    private final int quantity;

    public Quantity(final int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Quantity other = (Quantity) o;
        return quantity == other.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
