package cart.application.repository;

import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.CouponPolicy;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {

    Coupon findById(final Long id);

    List<Coupon> findByMemberId(final Long memberId);

    List<Coupon> findAllByOrderId(final Long orderId);

    Optional<CouponPolicy> findPercentCouponById(final Long memberCouponId);

    Optional<CouponPolicy> findAmountCouponById(final Long memberCouponId);

    void convertToUseMemberCoupon(final Long memberCouponId);

    long createOrderedCoupon(final Long orderId, final Long memberCouponId);

}
