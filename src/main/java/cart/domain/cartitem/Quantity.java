package cart.domain.cartitem;

public class Quantity {

    private final int quantity;

    private Quantity(final int quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    public static Quantity from(final int quantity) {
        return new Quantity(quantity);
    }

    private void validate(final int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1개 이상이어야 합니다");
        }
    }

    public int getQuantity() {
        return quantity;
    }
}
