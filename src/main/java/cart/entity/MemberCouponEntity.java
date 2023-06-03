package cart.entity;

import cart.domain.MemberCoupon;

public class MemberCouponEntity {

    private Long id;
    private Long memberId;
    private Long couponId;
    private Boolean used;

    public MemberCouponEntity(Long memberId, Long couponId, Boolean used) {
        this(null, memberId, couponId, used);
    }

    public MemberCouponEntity(Long id, Long memberId, Long couponId, Boolean used) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.used = used;
    }

    public static MemberCouponEntity from(MemberCoupon memberCoupon) {
        return new MemberCouponEntity(memberCoupon.getId(), memberCoupon.getMemberId(),
                memberCoupon.getCoupon().getId(), memberCoupon.isUsed());
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public boolean isUsed() {
        return used;
    }
}
