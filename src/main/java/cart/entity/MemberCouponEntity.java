package cart.entity;

public class MemberCouponEntity {

    private final Long id;
    private final Long couponId;
    private final Long memberId;
    private final boolean used;

    public MemberCouponEntity(final Long couponId, final Long memberId, final boolean used) {
        this(null, couponId, memberId, used);
    }

    public MemberCouponEntity(final Long id, final Long couponId, final Long memberId, final boolean used) {
        this.id = id;
        this.couponId = couponId;
        this.memberId = memberId;
        this.used = used;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public boolean isUsed() {
        return used;
    }
}
