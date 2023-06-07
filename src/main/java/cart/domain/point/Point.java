package cart.domain.point;

import cart.domain.product.Price;
import java.math.BigDecimal;

public class Point {

    private static final BigDecimal MIN_POINT = BigDecimal.ZERO;
    private static final Double EARN_POINT_RATE = 2.5 / 100;

    private final BigDecimal point;

    public Point(final BigDecimal point) {
        validatePositive(point);
        this.point = point;
    }

    private void validatePositive(final BigDecimal point) {
        if (MIN_POINT.compareTo(point) > 0) {
            throw new IllegalArgumentException("포인트는 최소 " + MIN_POINT + " 포인트 이상이어야합니다.");
        }
    }

    public static Point calculateFromPrice(final Price price) {
        return new Point(price.multiplyAndRound(EARN_POINT_RATE).price());
    }

    public Point add(final Point other) {
        return new Point(this.point.add(other.point));
    }

    public Point subtract(final Point other) {
        return new Point(this.point.subtract(other.point));
    }

    public boolean isMoreThan(final Point other) {
        return this.point.compareTo(other.point) > 0;
    }

    public BigDecimal getPoint() {
        return point;
    }
}
