package cart.entity;

import java.time.LocalDateTime;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final LocalDateTime createdAt;

    public OrderEntity(final Long id, final Long memberId, final LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.createdAt = createdAt;
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
}
