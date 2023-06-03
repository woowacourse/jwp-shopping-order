package cart.application.repository;

import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.CouponPolicy;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    List<Coupon> findByMemberId(Long memberId);

    Optional<CouponPolicy> findPercentCouponById(Long memberCouponId);

    Optional<CouponPolicy> findAmountCouponById(Long memberCouponId);

    void convertToUseMemberCoupon(Long memberCouponId);

    long createOrderedCoupon(Long orderId, final Long memberCouponId);

    List<Coupon> findUsedCouponByOrderId(Long orderId);
}
