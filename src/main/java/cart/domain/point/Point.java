package cart.domain.point;

import cart.domain.common.Money;

import java.util.Objects;

public class Point {

    private final Money money;

    public Point(final Money money) {
        this.money = money;
    }

    public static Point valueOf(final int point) {
        return new Point(Money.valueOf(point));
    }

    public Point reduce(final Point point) {
        return new Point(this.money.subtract(point.money));
    }

    public Point save(final Point point) {
        return new Point(this.money.add(point.money));
    }

    public boolean isMoreThan(final Point point) {
        return money.isMoreThan(point.money);
    }

    public int getMoneyAmount() {
        return money.getAmount();
    }

    public Money getMoney() {
        return money;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Point point1 = (Point) o;
        return Objects.equals(money, point1.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }

    @Override
    public String toString() {
        return "Point{" +
                "amount=" + money +
                '}';
    }
}
