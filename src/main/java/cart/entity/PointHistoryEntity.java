package cart.entity;

import java.util.Objects;

public class PointHistoryEntity {

    private final Long id;
    private final Long orderId;
    private final Long pointId;
    private final int usedPoint;

    public PointHistoryEntity(Long id, Long orderId, Long pointId, int usedPoint) {
        this.id = id;
        this.orderId = orderId;
        this.pointId = pointId;
        this.usedPoint = usedPoint;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getPointId() {
        return pointId;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointHistoryEntity that = (PointHistoryEntity) o;
        return usedPoint == that.usedPoint && Objects.equals(id, that.id) && Objects.equals(orderId, that.orderId) && Objects.equals(pointId, that.pointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, pointId, usedPoint);
    }

    @Override
    public String toString() {
        return "PointHistoryEntity{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", pointId=" + pointId +
                ", usedPoint=" + usedPoint +
                '}';
    }
}
