package cart.entity;

import java.sql.Timestamp;

public class OrderEntity {
    private final Long id;
    private final Long memberId;
    private final Timestamp orderDate;

    public OrderEntity(Long id, Long memberId, Timestamp orderDate) {
        this.id = id;
        this.memberId = memberId;
        this.orderDate = orderDate;
    }

    public OrderEntity(Long memberId) {
        this(null, memberId, null);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }
}
