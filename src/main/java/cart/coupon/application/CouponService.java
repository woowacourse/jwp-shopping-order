package cart.coupon.application;

import cart.cart.Cart;
import cart.coupon.Coupon;
import cart.discountpolicy.application.DiscountPolicyService;
import cart.discountpolicy.discountcondition.DiscountCondition;
import org.springframework.stereotype.Service;

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
        final var couponId = couponRepository.save(name, policyId);
        return couponId;
    }

    public void applyCoupon(Long couponId, Cart cart) {
        discountPolicyService.applyPolicy(couponId, cart);
    }

    public Coupon findById(Long couponId) {
        return couponRepository.findById(couponId);
    }
}
