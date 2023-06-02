package cart.entity;

public class MemberCouponEntity {

    private final Long id;
    private final boolean isUsed;
    private final Long memberId;
    private final Long couponId;

    private MemberCouponEntity(final Long id, final boolean isUsed, final Long memberId, final Long couponId) {
        this.id = id;
        this.isUsed = isUsed;
        this.memberId = memberId;
        this.couponId = couponId;
    }

    public static MemberCouponEntity of(final long id, final boolean isUsed, final long memberId, final long couponId) {
        return new MemberCouponEntity(id, isUsed, memberId, couponId);
    }

    public Long getId() {
        return id;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getCouponId() {
        return couponId;
    }
}
