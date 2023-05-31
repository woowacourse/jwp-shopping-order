package cart.entity;

public class OrdersEntity {
    private final Long id;
    private final Long deliveryFee;
    private final Long memberCouponId;
    private final Long memberId;

    public OrdersEntity(final Long deliveryFee, final Long memberCouponId, final Long memberId) {
        this(null, deliveryFee, memberCouponId, memberId);
    }

    public OrdersEntity(final Long id, final Long deliveryFee, final Long memberCouponId, final Long memberId) {
        this.id = id;
        this.deliveryFee = deliveryFee;
        this.memberCouponId = memberCouponId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public Long getMemberCouponId() {
        return memberCouponId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
