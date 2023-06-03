package cart.coupon.application;

import cart.coupon.Coupon;
import cart.discountpolicy.discountcondition.DiscountTarget;

import java.util.List;

public interface CouponRepository {
    Long save(String name, Long discountPolicyId);

    Coupon findById(Long id);

    List<Coupon> findAllByMemberId(Long memberId);

    void giveCouponToMember(Long memberId, Long couponId);

    List<Coupon> findAllByMemberIdExcludingTarget(Long memberId, List<DiscountTarget> discountTargets);

    List<Coupon> findAllByMemberIdApplyingToTotalPrice(Long memberId);
}

