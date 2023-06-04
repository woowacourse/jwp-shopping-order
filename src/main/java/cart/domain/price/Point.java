package cart.domain.price;

import cart.exception.NumberRangeException;

public class Point {
    private final long amount;

    public Point(long amount) {
        validate(amount);
        this.amount = amount;
    }

    private void validate(long amount) {
        if (amount < 0) {
            throw new NumberRangeException("point", "포인트는 음수가 될 수 없습니다.");
        }
    }

    public Point plus(Point point) {
        return new Point(this.amount + point.amount);
    }

    public Point minus(Point point) {
        return new Point(this.amount - point.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Point)) {
            return false;
        }

        Point point = (Point) o;

        return amount == point.amount;
    }

    @Override
    public int hashCode() {
        return (int) (amount ^ (amount >>> 32));
    }

    public long getAmount() {
        return amount;
    }
}
