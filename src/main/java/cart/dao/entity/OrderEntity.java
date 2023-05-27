package cart.dao.entity;

import java.time.LocalDateTime;

public class OrderEntity {

    private Long id;
    private Long memberId;
    private Integer usedPoint;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderEntity(Long id, Long memberId, Integer usedPoint, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
