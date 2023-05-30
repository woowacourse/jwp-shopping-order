package cart.domain;

import cart.domain.common.Money;

import java.util.Objects;

public class Point {

    private final Money amount;

    public Point(final Money amount) {
        this.amount = amount;
    }

    public static Point valueOf(final int point) {
        return new Point(Money.valueOf(point));
    }

    public Point reduce(final Point point) {
        return new Point(this.amount.subtract(point.amount));
    }

    public Point save(final Point point) {
        return new Point(this.amount.add(point.amount));
    }

    public boolean isMoreThan(final Point point) {
        return amount.isMoreThan(point.amount);
    }

    public Money getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Point point1 = (Point) o;
        return Objects.equals(amount, point1.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
