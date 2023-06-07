package cart.domain.vo;

import java.util.Objects;

public class Price {

    private final int price;

    public Price(int price) {
        this.price = price;
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
