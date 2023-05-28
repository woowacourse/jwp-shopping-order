package cart.entity;

public class OrdersEntity {
    private final Long id;
    private final Long deliveryFee;
    private final Long couponId;
    private final Long memberId;

    public OrdersEntity(final Long deliveryFee, final Long couponId, final Long memberId) {
        this(null, deliveryFee, couponId, memberId);
    }

    public OrdersEntity(final Long id, final Long deliveryFee, final Long couponId, final Long memberId) {
        this.id = id;
        this.deliveryFee =deliveryFee;
        this.couponId = couponId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
