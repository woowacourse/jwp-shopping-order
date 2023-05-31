package cart.dao.entity;

import java.util.Objects;

public class OrderEntity {

    private final long memberId;
    private final Long deliveryFee;
    private Long id;

    public OrderEntity(final long memberId, final long deliveryFee) {
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
    }

    public OrderEntity(final long id, final long memberId, final long deliveryFee) {
        this.id = id;
        this.memberId = memberId;
        this.deliveryFee = deliveryFee;
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
