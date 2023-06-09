package cart.domain.order;

import java.util.Objects;

public class Quantity {

    private static final int MINIMUM_QUANTITY = 0;

    private final int quantity;

    public Quantity(final int quantity) {
        if (quantity <= MINIMUM_QUANTITY) {
            throw new IllegalArgumentException(String.format("수량은 %s 이하 일 수 없습니다.", MINIMUM_QUANTITY));
        }

        this.quantity = quantity;
    }

    public static Quantity from(final int quantity) {
        return new Quantity(quantity);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Quantity quantity1 = (Quantity) o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }

    public int getQuantity() {
        return quantity;
    }
}
