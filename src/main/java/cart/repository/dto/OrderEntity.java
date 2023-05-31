package cart.repository.dto;

import java.time.LocalDateTime;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final LocalDateTime createdAt;
    private final long spendPoint;

    public OrderEntity(Long id, Long memberId, LocalDateTime createdAt, long spendPoint) {
        this.id = id;
        this.memberId = memberId;
        this.createdAt = createdAt;
        this.spendPoint = spendPoint;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getSpendPoint() {
        return spendPoint;
    }
}
