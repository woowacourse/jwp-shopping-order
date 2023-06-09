package cart.domain;

import cart.exception.illegalexception.IllegalQuantityException;
import java.util.Objects;

public class Quantity {

    private static final int MIN_COUNT = 1;
    public static final Quantity DEFAULT = Quantity.from(MIN_COUNT);
    private final int count;

    public Quantity(int count) {
        this.count = count;
    }

    public static Quantity from(int count) {
        validate(count);
        return new Quantity(count);
    }

    private static void validate(int count) {
        if (count < MIN_COUNT) {
            throw new IllegalQuantityException("상품 수량은 1개 이상이어야 합니다.");
        }
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
        return count == quantity.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }

    public int getCount() {
        return count;
    }
}
