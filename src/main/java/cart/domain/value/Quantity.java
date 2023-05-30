package cart.domain.value;

public class Quantity {

    private final int quantity;

    public Quantity(final int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(final int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("상품 개수는 0이하가 될 수 없습니다.");
        }
    }

    public int getQuantity() {
        return quantity;
    }
}
