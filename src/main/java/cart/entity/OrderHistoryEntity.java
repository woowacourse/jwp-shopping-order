package cart.entity;

public class OrderHistoryEntity {
    private final Long id;
    private final Long memberId;
    private final int originalPrice;
    private final int usedPoint;
    private final int totalPrice;

    public OrderHistoryEntity(
            final Long id,
            final Long memberId,
            final int originalPrice,
            final int usedPoint,
            final int totalPrice
    ) {
        this.id = id;
        this.memberId = memberId;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.totalPrice = totalPrice;
    }

    public OrderHistoryEntity(
            final Long memberId,
            final int originalPrice,
            final int usedPoint,
            final int totalPrice
    ) {
        this(null, memberId, originalPrice, usedPoint, totalPrice);
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

    public int getTotalPrice() {
        return totalPrice;
    }
}
