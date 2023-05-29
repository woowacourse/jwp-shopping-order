package cart.dao.entity;

public class OrdersCouponEntity {
    private final Long id;
    private final Long ordersId;
    private final Long couponId;

    public OrdersCouponEntity(Long id, Long ordersId, Long couponId) {
        this.id = id;
        this.ordersId = ordersId;
        this.couponId = couponId;
    }

    public Long getId() {
        return id;
    }

    public Long getOrdersId() {
        return ordersId;
    }

    public Long getCouponId() {
        return couponId;
    }
}
