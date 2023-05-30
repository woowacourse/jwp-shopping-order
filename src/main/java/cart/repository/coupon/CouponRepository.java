package cart.repository.coupon;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
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
