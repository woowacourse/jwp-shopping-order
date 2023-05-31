package cart.domain;

import java.util.Objects;

public final class Price {

    private final int amount;

    public Price(final int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price price = (Price) o;
        return amount == price.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
