package cart.domain;

import cart.exception.InvalidPriceException;

import java.util.Objects;

public final class Price {

    private static final int BOUNDARY_AMOUNT = 0;

    private final int amount;

    public Price(final int amount) {
        validateNotNegative(amount);
        this.amount = amount;
    }

    public Price add(final Price price) {
        return new Price(amount + price.amount);
    }

    public Price subtract(final Price price) {
        return new Price(amount - price.amount);
    }

    public boolean isGreaterThanOrEqualTo(final Price price) {
        return amount >= price.amount;
    }

    private void validateNotNegative(final int amount) {
        if (amount < BOUNDARY_AMOUNT) {
            throw new InvalidPriceException();
        }
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
