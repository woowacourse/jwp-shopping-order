package cart.coupon.application;

import cart.cart.Cart;
import cart.cart.presentation.dto.CouponResponse;
import cart.coupon.Coupon;
import cart.discountpolicy.application.DiscountPolicyService;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.discountpolicy.discountcondition.DiscountTarget;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {
    private final DiscountPolicyService discountPolicyService;
    private final CouponRepository couponRepository;

    public CouponService(DiscountPolicyService discountPolicyService, CouponRepository couponRepository) {
        this.discountPolicyService = discountPolicyService;
        this.couponRepository = couponRepository;
    }

    public Long saveCoupon(DiscountCondition discountCondition, String name) {
        final var policyId = discountPolicyService.savePolicy(discountCondition);
        return couponRepository.save(name, policyId);
    }

    public Coupon findById(Long couponId) {
        return couponRepository.findById(couponId);
    }

    public List<CouponResponse> findCouponsByMember(Long memberId) {
        return couponRepository.findAllByMemberId(memberId)
                .stream().map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    public void applyCoupons(Cart cart, List<Long> couponIds) {
        final var coupons = couponIds.stream()
                .map(couponRepository::findById)
                .collect(Collectors.toList());

        for (Coupon coupon : coupons) {
            coupon.apply(cart);
        }
    }

    public void applyCouponsApplyingToTotalPrice(Long memberId, Cart cart) {
        for (Coupon coupon : couponRepository.findAllByMemberIdApplyingToTotalPrice(memberId)) {
            coupon.apply(cart);
        }
    }
}
