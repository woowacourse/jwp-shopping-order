package cart.entity.order;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrderTableEntity {

    private final Long id;
    private final Long memberId;
    private final LocalDateTime createAt;
    private final int deliveryFee;

    public OrderTableEntity(final Long id, final Long memberId, final Timestamp createAt, final int deliveryFee) {
        this.id = id;
        this.memberId = memberId;
        this.createAt = createAt.toLocalDateTime();
        this.deliveryFee = deliveryFee;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }
}
