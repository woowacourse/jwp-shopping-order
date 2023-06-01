package cart.dao.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final int usedPoint;
    private final int savedPoint;
    private final int deliveryFee;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public OrderEntity(final Long id, final Long memberId, final int usedPoint, final int savedPoint, final int deliveryFee,
                       final LocalDateTime createdAt,
                       final LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.deliveryFee = deliveryFee;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getSavedPoint() {
        return savedPoint;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
