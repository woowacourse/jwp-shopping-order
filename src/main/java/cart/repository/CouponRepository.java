package cart.repository;

import cart.domain.Coupon;
import cart.domain.Coupons;
import cart.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {

    Long issue(final Member member, final Long couponId);

    void changeStatus(final Long couponId, final Long memberId);

    Coupons findCouponsByMemberId(final Long memberId);

    Coupons findCouponAll();

    Coupon findCouponByCouponIdAndMemberId(final Long couponId, final Long memberId);

    void deleteCoupon(final Long id);
}
