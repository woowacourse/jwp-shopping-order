package cart.entity;

import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final LocalDateTime orderedAt;
    private final int usedPoint;

    public OrderEntity(final Long id, final Long memberId, final LocalDateTime orderedAt, final int usedPoint) {
        this.id = id;
        this.memberId = memberId;
        this.orderedAt = orderedAt;
        this.usedPoint = usedPoint;
    }

    public static OrderEntity toCreate(final Long memberId, final int usedPoint) {
        return new OrderEntity(null, memberId, null, usedPoint);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public int getUsedPoint() {
        return usedPoint;
    }
}
