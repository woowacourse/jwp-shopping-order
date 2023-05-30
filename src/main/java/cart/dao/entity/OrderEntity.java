package cart.dao.entity;

import java.time.LocalDateTime;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final int usedPoint;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public OrderEntity(final Long id, final Long memberId, final int usedPoint, final LocalDateTime createdAt,
                       final LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
}
