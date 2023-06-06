package cart.entity;

public class OrderEntity {

    private final Long id;
    private final long memberId;
    private final long shippingFee;
    private final long totalProductsPrice;
    private final long usedPoint;
    private final String createdAt;

    public OrderEntity(final Long id, final long memberId, final long shippingFee, final long totalProductsPrice, final long usedPoint, final String createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.shippingFee = shippingFee;
        this.totalProductsPrice = totalProductsPrice;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
    }

    public static OrderEntity of(final long memberId, final long shippingFee, final long totalPrice, final long usedPoint) {
        return new OrderEntity(null, memberId, shippingFee, totalPrice, usedPoint, null);
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public long getShippingFee() {
        return shippingFee;
    }

    public long getTotalProductsPrice() {
        return totalProductsPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getUsedPoint() {
        return usedPoint;
    }
}
