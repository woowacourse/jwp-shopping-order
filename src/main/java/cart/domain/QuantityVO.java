package cart.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuantityVO)) return false;
        QuantityVO that = (QuantityVO) o;
        return quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
