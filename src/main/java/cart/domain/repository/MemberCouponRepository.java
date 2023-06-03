package cart.domain.repository;

import cart.domain.MemberCoupon;
import java.util.List;

public interface MemberCouponRepository {

    MemberCoupon findByCouponIdAndMemberId(final Long couponId, final Long memberId);

    List<Long> findCouponIdsByMemberId(final Long memberId);

    void update(final MemberCoupon coupon, final Long memberId);
}
