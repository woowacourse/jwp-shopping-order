package cart.domain.product;

import java.util.Objects;

public class Price {

    private static final int MINIMUM_PRICE = 1;

    private final int value;

    public Price(final int price) {
        validatePrice(price);
        this.value = price;
    }

    private void validatePrice(final int price) {
        if (price < MINIMUM_PRICE) {
            throw new IllegalArgumentException("최소 가격은 " + MINIMUM_PRICE + "원 입니다.");
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Price price = (Price) o;
        return value == price.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
