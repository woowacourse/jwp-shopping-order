package cart.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final LocalDateTime orderedAt;
    private final int usedPoint;
    private final int savedPoint;

    public OrderEntity(
            final Long id,
            final Long memberId,
            final LocalDateTime orderedAt,
            final int usedPoint,
            final int savedPoint
    ) {
        this.id = id;
        this.memberId = memberId;
        this.orderedAt = orderedAt;
        this.usedPoint = usedPoint;
        this.savedPoint = savedPoint;
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
        final OrderEntity that = (OrderEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
