package cart.entity;

import java.time.LocalDateTime;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final int usedPoint;
    private final int savedPoint;
    private final LocalDateTime orderedAt;

    public OrderEntity(Long id, Long memberId, int usedPoint, int savedPoint, LocalDateTime orderedAt) {
        this.id = id;
        this.memberId = memberId;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.orderedAt = orderedAt;
    }

    public Long getId() {
        return id;
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

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
