package cart.application.repository.coupon;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;

import java.util.List;

public interface CouponRepository {

    Coupons findByMemberId(final Long memberId);
    Coupons findMemberCouponByCouponIds(final Long memberId,final List<Long> couponIds);

    void convertToUseMemberCoupon(final Coupon coupon);

    void createOrderedCoupon(final Long orderId, final Coupon coupon);

    Coupons findCouponByOrderId(Long memberId, Long orderId);

}
