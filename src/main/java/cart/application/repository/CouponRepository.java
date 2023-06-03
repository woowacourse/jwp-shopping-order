package cart.application.repository;

import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.CouponPolicy;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    List<Coupon> findByMemberId(final Long memberId);

    Optional<CouponPolicy> findPercentCouponById(final Long memberCouponId);

    Optional<CouponPolicy> findAmountCouponById(final Long memberCouponId);

    void convertToUseMemberCoupon(final Long memberCouponId);

    long createOrderedCoupon(final Long orderId, final Long memberCouponId);

    List<Coupon> findUsedCouponByOrderId(final Long orderId);
}
