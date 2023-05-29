package cart.entity;

import cart.domain.MemberCoupon;

public class MemberCouponEntity {

    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final boolean used;

    public MemberCouponEntity(final Long memberId, final Long couponId, final boolean used) {
        this(null, memberId, couponId, used);
    }

    public MemberCouponEntity(final Long id, final Long memberId, final Long couponId, final boolean used) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.used = used;
    }

    public static MemberCouponEntity from(final MemberCoupon memberCoupon) {
        return new MemberCouponEntity(memberCoupon.getId(), memberCoupon.getMember().getId(), memberCoupon.getCoupon().getId(), memberCoupon.isUsed());
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

    public boolean isUsed() {
        return used;
    }
}
