package cart.domain.point;

public class PointHistory {

    private final Long orderId;
    private final Point earnedPoint;
    private final Point usedPoint;

    public PointHistory(final long orderId, final Point earnedPoint, final Point usedPoint) {
        this.orderId = orderId;
        this.earnedPoint = earnedPoint;
        this.usedPoint = usedPoint;
    }

    public int calculatePoint() {
        return earnedPoint.minus(usedPoint);
    }

    public long getOrderId() {
        return orderId;
    }

    public int getEarnedPoint() {
        return earnedPoint.getPoint();
    }

    public int getUsedPoint() {
        return usedPoint.getPoint();
    }

}
