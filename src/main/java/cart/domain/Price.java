package cart.domain;

import java.util.Objects;

public class Price {
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

    public Price discount(Percentage percentage) {
        return new Price(value - (int) (value * percentage.getPercentage()));
    }

    public Price plus(Price price) {
        return new Price(this.value + price.value);
    }

    public Price minus(Price price) {
        return new Price(this.value - price.value);
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
