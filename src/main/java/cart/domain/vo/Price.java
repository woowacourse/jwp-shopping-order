package cart.domain.vo;

import cart.exception.CartItemException;

import java.util.Objects;

public class Price {

    public static final int MINIMUM_PRICE = 1;

    private final int price;

    public Price(int price) {
        validate(price);
        this.price = price;
    }

    private void validate(int price) {
        if (price < MINIMUM_PRICE) {
            throw new CartItemException.InvalidPrice();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price that = (Price) o;
        return price == that.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public int getPrice() {
        return price;
    }
}
