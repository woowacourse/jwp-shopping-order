package cart.domain;

public final class QuantityVO {

    private int quantity;

    public QuantityVO(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 자연수여야 합니다.");
        }
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
