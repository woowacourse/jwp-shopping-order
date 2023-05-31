package cart.entity;

import cart.domain.Order;

public class OrderHistoryEntity {
    private final Long id;
    private final Long memberId;
    private final int usedPoint;
    private final int orderPrice;

    public OrderHistoryEntity(
            final Long id,
            final Long memberId,
            final int usedPoint,
            final int orderPrice
    ) {
        this.id = id;
        this.memberId = memberId;
        this.usedPoint = usedPoint;
        this.orderPrice = orderPrice;
    }

    public OrderHistoryEntity(
            final Long memberId,
            final int usedPoint,
            final int orderPrice
    ) {
        this(null, memberId, usedPoint, orderPrice);
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

    public int getOrderPrice() {
        return orderPrice;
    }

    public Order toOrder() {
        return null;
    }
}
