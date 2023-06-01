package cart.domain;

import cart.exception.OrderException.NegativePoint;
import cart.exception.OrderException.LackOfPoint;

public class Point {

    private final long point;

    public Point(final long point) {
        validateNegative(point);
        this.point = point;
    }

    private void validateNegative(final long point) {
        if(point < 0){
            throw new NegativePoint();
        }
    }

    public Point use(final Point usedPoint) {
        if (this.point < usedPoint.point) {
            throw new LackOfPoint();
        }
        return new Point(point - usedPoint.point);
    }

    public Point add(final Point earnedPoint) {
        return new Point(this.point + earnedPoint.point);
    }

    public long getPoint() {
        return point;
    }
}
