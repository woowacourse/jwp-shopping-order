package cart.entity;

public class OrderCouponEntity {
    private final Long id;
    private final Long couponId;
    private final Long orderId;

    public OrderCouponEntity(Long couponId, Long orderId) {
        this(null,couponId,orderId);
    }

    public OrderCouponEntity(Long id, Long couponId, Long orderId) {
        this.id = id;
        this.couponId = couponId;
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
