package cart.domain.point;

import cart.exception.OverFullPointException;

public class Point {
    private final int point;

    public Point(final int point) {
        validatePositive(point);

        this.point = point;
    }

    public void validateOverPoint(final int totalPoint) {
        if (totalPoint < point) {
            throw new IllegalArgumentException("최대 " + totalPoint + "포인트 사용할 수 있습니다.");
        }
    }

    public int applyDisCount(final int totalAmount) {
        if (totalAmount < point) {
            throw new OverFullPointException("포인트는 최대 결제금액만큼 사용할 수 있습니다.");
        }
        return totalAmount - point;
    }

    public int minus(final Point point) {
        return this.point - point.point;
    }

    public void validatePositive(final int point) {
        if (point < 0) {
            throw new IllegalArgumentException("포인트는 0 이상 이여야 합니다.");
        }
    }

    public int getPoint() {
        return point;
    }
}
