package cart.entity;

public class OrderHistoryEntity {
    private final Long id;
    private final Long memberId;
    private final int originalPrice;
    private final int usedPoint;
    private final int orderPrice;

    public OrderHistoryEntity(
            final Long id,
            final Long memberId,
            final int originalPrice,
            final int usedPoint,
            final int orderPrice
    ) {
        this.id = id;
        this.memberId = memberId;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.orderPrice = orderPrice;
    }

    public OrderHistoryEntity(
            final Long memberId,
            final int originalPrice,
            final int usedPoint,
            final int orderPrice
    ) {
        this(null, memberId, originalPrice, usedPoint, orderPrice);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    @Override
    public String toString() {
        return "OrderHistoryEntity{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", originalPrice=" + originalPrice +
                ", usedPoint=" + usedPoint +
                ", totalPrice=" + orderPrice +
                '}';
    }
}
