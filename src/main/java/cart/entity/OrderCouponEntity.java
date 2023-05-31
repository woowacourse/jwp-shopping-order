package cart.entity;

public class OrderCouponEntity {

    private Long id;
    private Long orderItemId;
    private Long couponId;

    public OrderCouponEntity(Long orderItemId, Long couponId) {
        this(null, orderItemId, couponId);
    }

    public OrderCouponEntity(Long id, Long orderItemId, Long couponId) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.couponId = couponId;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public Long getCouponId() {
        return couponId;
    }
}
