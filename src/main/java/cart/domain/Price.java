package cart.domain;

import java.util.Objects;

public class Price {
    public static final Price DELIVERY = new Price(3000);
    private final int value;

    public Price(int value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("가격은 음수 일 수 없습니다.");
        }
    }

    public Price discount(Percent percent) {
        return new Price(value - (int) (value * percent.getPercentage()));
    }

    public Price plus(Price price) {
        return new Price(this.value + price.value);
    }

    public Price minus(Price price) {
        return new Price(this.value - price.value);
    }

    public boolean isMoreThan(Price price) {
        return this.value > price.value;
    }

    public boolean isLessThan(Price price) {
        return this.value < price.value;
    }

    public boolean isZero() {
        return value == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return value == price.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int getValue() {
        return value;
    }
}
