package cart.entity;

import cart.domain.Order;

public class OrderHistoryEntity {
    private final Long id;
    private final Long memberId;
    private final int totalPrice;
    private final int usedPoint;
    private final int orderPrice;

    public OrderHistoryEntity(
            final Long id,
            final Long memberId,
            final int totalPrice,
            final int usedPoint,
            final int orderPrice
    ) {
        this.id = id;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.orderPrice = orderPrice;
    }

    public OrderHistoryEntity(
            final Long memberId,
            final int totalPrice,
            final int usedPoint,
            final int orderPrice
    ) {
        this(null, memberId, totalPrice, usedPoint, orderPrice);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getTotalPrice() {
        return totalPrice;
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
