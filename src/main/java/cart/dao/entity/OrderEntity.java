package cart.dao.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final Integer usedPoint;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
