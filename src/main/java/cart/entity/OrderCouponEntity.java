package cart.entity;

public class OrderCouponEntity {
    private final Long id;
    private final Long memberCouponId;
    private final Long orderId;

    public OrderCouponEntity(Long memberCouponId, Long orderId) {
        this(null,memberCouponId,orderId);
    }

    public OrderCouponEntity(Long id, Long memberCouponId, Long orderId) {
        this.id = id;
        this.memberCouponId = memberCouponId;
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberCouponId() {
        return memberCouponId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
