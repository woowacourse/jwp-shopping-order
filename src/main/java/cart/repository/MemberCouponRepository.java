package cart.repository;

import cart.domain.MemberCoupon;

public interface MemberCouponRepository {

    MemberCoupon findByCouponIdAndMemberId(final Long couponId, final Long memberId);

    void update(final MemberCoupon coupon, final Long memberId);
}
