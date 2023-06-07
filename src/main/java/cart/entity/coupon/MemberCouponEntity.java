package cart.entity.coupon;

public class MemberCouponEntity {

    private final Long id;
    private final Long couponId;
    private final Long memberId;

    public MemberCouponEntity(final Long id, final Long couponId, final Long memberId) {
        this.id = id;
        this.couponId = couponId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
