package cart.domain;

public class Point {
    private final long amount;

    public Point(long amount) {
        validate(amount);
        this.amount = amount;
    }

    private void validate(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("포인트는 음수가 될 수 없습니다.");
        }
    }

    public long getAmount() {
        return amount;
    }

    public Point plus(Point point) {
        return new Point(this.amount + point.amount);
    }

    public Point minus(Point point) {
        return new Point(this.amount - point.amount);
    }
}
