package cart.domain.point;

public class UsedPoint {

    private Long id;
    private final Long pointId;
    private final Integer usedPoint;

    public UsedPoint(final Long pointId, final Integer usedPoint) {
        this.pointId = pointId;
        this.usedPoint = usedPoint;
    }

    public UsedPoint(final Long id, final Long pointId, final Integer usedPoint) {
        this.id = id;
        this.pointId = pointId;
        this.usedPoint = usedPoint;
    }

    public Long getId() {
        return id;
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
                "id=" + id +
                ", pointId=" + pointId +
                ", usedPoint=" + usedPoint +
                '}';
    }
}
