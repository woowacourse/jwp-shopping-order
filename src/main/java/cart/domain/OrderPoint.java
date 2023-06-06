package cart.domain;

public class OrderPoint {

    private final Long pointId;
    private final Point usedPoint;
    private final Point earnedPoint;

    public OrderPoint(final Long pointId, final Point usedPoint, final Point earnedPoint) {
        this.pointId = pointId;
        this.usedPoint = usedPoint;
        this.earnedPoint = earnedPoint;
    }

    public Long getPointId() {
        return pointId;
    }

    public Point getUsedPoint() {
        return usedPoint;
    }

    public Point getEarnedPoint() {
        return earnedPoint;
    }
}
