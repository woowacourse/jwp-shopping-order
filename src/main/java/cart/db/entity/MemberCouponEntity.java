package cart.db.entity;

import cart.domain.coupon.MemberCoupon;

public class MemberCouponEntity {

    private Long id;
    private Boolean isUsed;
    private Long memberId;
    private Long couponId;

    public MemberCouponEntity(final Boolean isUsed, final Long memberId, final Long couponId) {
        this.isUsed = isUsed;
        this.memberId = memberId;
        this.couponId = couponId;
    }

    public MemberCouponEntity(Long id, Boolean isUsed, Long memberId, Long couponId) {
        this.id = id;
        this.isUsed = isUsed;
        this.memberId = memberId;
        this.couponId = couponId;
    }

    public static MemberCouponEntity of(final MemberCoupon memberCoupon) {
        return new MemberCouponEntity(
                memberCoupon.getId(),
                memberCoupon.isUsed(),
                memberCoupon.getOwnerId(),
                memberCoupon.getCouponId()
        );
    }

    public Long getId() {
        return id;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getCouponId() {
        return couponId;
    }
}
