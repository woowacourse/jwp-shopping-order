package cart.entity;

public class OrderCouponEntity {

    private Long id;
    private Long orderItemId;
    private Long memberCouponId;

    public OrderCouponEntity(Long orderItemId, Long memberCouponId) {
        this(null, orderItemId, memberCouponId);
    }

    public OrderCouponEntity(Long id, Long orderItemId, Long memberCouponId) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.memberCouponId = memberCouponId;
    }


    public Long getId() {
        return id;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public Long getMemberCouponId() {
        return memberCouponId;
    }
}
