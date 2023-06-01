package cart.domain.coupon;

import cart.domain.member.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {

    Long issue(final Member member, final Long couponId);

    void changeStatusTo(final Long couponId, final Long memberId, final Boolean toChange);

    Coupons findCouponsByMemberId(final Long memberId);

    Coupons findCouponAll();

    Coupon findCouponByCouponIdAndMemberId(final Long couponId, final Long memberId);

    void deleteCoupon(final Long id);

    Coupon findCouponById(final Long couponId);
}
