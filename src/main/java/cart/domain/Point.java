package cart.domain;

import cart.exception.IllegalPointException;
import java.util.Objects;

public class Point {

    private static final int MIN_VALUE = 0;
    private static final double EARN_RATE = 2.5 / 100;
    private final int value;

    public Point(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalPointException(String.format("포인트는 %d보다 작을 수 없습니다.", MIN_VALUE));
        }
    }

    public static Point fromMoney(Money money) {
        return new Point(money.multiplyRateAndRound(EARN_RATE).getIntValue());
    }

    public Money toMoney() {
        return Money.from(value);
    }

    public boolean isLessOrEqualThan(Point other) {
        return this.value <= other.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return value == point.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int getValue() {
        return value;
    }
}
