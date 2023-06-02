package cart.dao.entity;

import cart.domain.Order;
import cart.domain.OrderStatus;
import java.util.Objects;

public class OrderEntity {

    private final Long id;
    private final long memberId;
    private final long deliveryFee;
    private final String status;

    public OrderEntity(final Long id, final long memberId, final long deliveryFee, final String status) {
        this.id = id;
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
        this.status = status;
    }

    public OrderEntity(final long memberId, final long deliveryFee) {
        this(null, memberId, deliveryFee, OrderStatus.COMPLETE.getValue());
    }

    public static OrderEntity from(final Order order) {
        return new OrderEntity(
                order.getId(),
                order.getMember().getId(),
                order.getDeliveryFee().getValue(),
                order.getStatus().getValue());
    }

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderEntity that = (OrderEntity) o;
        return memberId == that.memberId && Objects.equals(deliveryFee, that.deliveryFee)
                && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, deliveryFee, id);
    }
}
