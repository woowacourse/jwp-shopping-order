package cart.entity;

public class OrderEntity {

    private final Long id;
    private final long memberId;
    private final long shippingFee;
    private final long totalPrice;
    private final String createdAt;

    public OrderEntity(final Long id, final long memberId, final long shippingFee, final long totalPrice, final String createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public static OrderEntity of(final long memberId, final long shippingFee, final long totalPrice) {
        return new OrderEntity(null, memberId, shippingFee, totalPrice, null);
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

    public long getTotalPrice() {
        return totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
