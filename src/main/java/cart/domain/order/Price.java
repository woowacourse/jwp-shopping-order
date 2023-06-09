package cart.domain.order;

import java.util.Objects;

public class Price {

    private static final int MINIMUM_PRICE = 0;

    private final int price;

    private Price(final int price) {
        if (price <= MINIMUM_PRICE) {
            throw new IllegalArgumentException(String.format("가격은 %s 이하 일 수 없습니다.", MINIMUM_PRICE));
        }

        this.price = price;
    }

    public static Price from(final int price) {
        return new Price(price);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Price price1 = (Price) o;
        return price == price1.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public int getPrice() {
        return price;
    }
}
