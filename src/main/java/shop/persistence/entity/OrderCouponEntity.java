package shop.persistence.entity;

public class OrderCouponEntity {
    private final Long id;
    private final Long orderId;
    private final Long couponId;

    public OrderCouponEntity(Long id, Long orderId, Long couponId) {
        this.id = id;
        this.orderId = orderId;
        this.couponId = couponId;
    }

    public OrderCouponEntity(Long orderId, Long couponId) {
        this(null, orderId, couponId);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCouponId() {
        return couponId;
    }
}
