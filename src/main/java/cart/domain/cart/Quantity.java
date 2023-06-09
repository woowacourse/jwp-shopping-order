package cart.domain.cart;

public class Quantity {

    private static final int MIN_QUANTITY_VALUE = 1;
    private final int quantity;

    public Quantity(final int quantity) {
        validatePositive(quantity);
        this.quantity = quantity;
    }

    public static Quantity minQuantity() {
        return new Quantity(MIN_QUANTITY_VALUE);
    }

    private void validatePositive(final int quantity) {
        if (MIN_QUANTITY_VALUE > quantity) {
            throw new IllegalArgumentException("수량은 1이상 양수여야합니다.");
        }
    }

    public int quantity() {
        return quantity;
    }
}
