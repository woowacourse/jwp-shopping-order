package cart.repository;

import cart.domain.Coupon;

public interface MemberCouponRepository {

    Coupon findByCouponIdAndMemberId(final Long couponId, final Long memberId);

    void update(final Coupon coupon, final Long memberId);
}
