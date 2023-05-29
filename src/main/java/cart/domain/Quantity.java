package cart.domain;

import java.util.Objects;

public class Quantity {
    private final int amount;

    public Quantity(int amount) {
        validate(amount);
        this.amount = amount;
    }

    private void validate(int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("수량은 최소 1개 이상 부터 가능합니다.");
        }
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quantity quantity = (Quantity) o;
        return getAmount() == quantity.getAmount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount());
    }
}
