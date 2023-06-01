package cart.persistence.entity;

import java.time.LocalDateTime;

public class OrderHistoryEntity {

    private final Long id;
    private final Long memberId;
    private final Integer totalAmount;
    private final Integer usedPoint;
    private final Integer savedPoint;
    private final LocalDateTime createdAt;

    public OrderHistoryEntity(final Long id, final Long memberId, final Integer totalAmount, final Integer usedPoint, final Integer savedPoint, final LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.totalAmount = totalAmount;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public Integer getSavedPoint() {
        return savedPoint;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
