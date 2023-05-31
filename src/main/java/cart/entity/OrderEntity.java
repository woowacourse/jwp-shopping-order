package cart.entity;

import java.sql.Timestamp;

public class OrderEntity {

    private final Long id;
    private final Long memberId;
    private final Timestamp orderTime;

    public OrderEntity(
            final Long id,
            final Long memberId,
            final Timestamp orderTime
    ) {
        this.id = id;
        this.memberId = memberId;
        this.orderTime = orderTime;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }
}
