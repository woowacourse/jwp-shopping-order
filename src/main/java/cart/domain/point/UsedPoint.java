package cart.domain.point;

public class UsedPoint {

    private final Long pointId;
    private final Integer usedPoint;

    public UsedPoint(final Long pointId, final Integer usedPoint) {
        this.pointId = pointId;
        this.usedPoint = usedPoint;
    }

    public Long getPointId() {
        return pointId;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    @Override
    public String toString() {
        return "UsedPoint{" +
                "pointId=" + pointId +
                ", usedPoint=" + usedPoint +
                '}';
    }
}
