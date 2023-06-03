package cart.dao.entity;

import cart.domain.Order;
import cart.domain.OrderStatus;
import java.sql.Timestamp;
import java.util.Objects;

public class OrderEntity {

    private final Long id;
    private final long memberId;
    private final Long couponId;
    private final long deliveryFee;
    private final String status;
    private final Timestamp createdAt;

    public OrderEntity(final Long id,
                       final long memberId,
                       final Long couponId,
                       final long deliveryFee,
                       final String status,
                       final Timestamp createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.createdAt = createdAt;
    }

    public OrderEntity(final long memberId, final Long couponId, final long deliveryFee) {
        this(null, memberId, couponId, deliveryFee, OrderStatus.COMPLETE.getValue(), null);
    }

    public static OrderEntity from(final Order order) {
        return new OrderEntity(
                order.getId(),
                order.getMember().getId(),
                order.getCouponId(),
                order.getDeliveryFee().getValue(),
                order.getStatus().getValue(),
                null);
    }

    public Long getId() {
        return id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public long getMemberId() {
        return memberId;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderEntity that = (OrderEntity) o;
        return memberId == that.memberId && deliveryFee == that.deliveryFee && Objects.equals(id, that.id)
                && Objects.equals(couponId, that.couponId) && Objects.equals(status, that.status)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, couponId, deliveryFee, status, createdAt);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", couponId=" + couponId +
                ", deliveryFee=" + deliveryFee +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
