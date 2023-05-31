package cart.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    public static final Price ZERO = Price.from(0);

    private final BigDecimal amount;

    private Price(BigDecimal amount) {
        validate(amount.longValue());
        this.amount = amount;
    }

    public static Price from(long amount) {
        return new Price(BigDecimal.valueOf(amount));
    }

    private static void validate(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("금액은 음수가 될 수 없습니다.");
        }
    }

    public Price plus(Price price) {
        return new Price(this.amount.add(price.amount));
    }

    public Price minus(Price price) {
        return new Price(this.amount.subtract(price.amount));
    }

    public Price multiply(int factor) {
        return new Price(this.amount.multiply(BigDecimal.valueOf(factor)));
    }

    public long getAmount() {
        return amount.longValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(amount, price.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
