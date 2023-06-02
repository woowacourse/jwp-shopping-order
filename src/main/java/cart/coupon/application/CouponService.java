package cart.coupon.application;

import cart.cart.Cart;
import cart.cart.presentation.dto.CouponResponse;
import cart.coupon.Coupon;
import cart.discountpolicy.application.DiscountPolicyService;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.member.Member;
import org.springframework.stereotype.Service;

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
        final var couponId = couponRepository.save(name, policyId);
        return couponId;
    }

    public void applyCoupon(Long couponId, Cart cart) {
        final var coupon = couponRepository.findById(couponId);
        discountPolicyService.applyPolicy(coupon.getDiscountConditionId(), cart);
    }

    public Coupon findById(Long couponId) {
        return couponRepository.findById(couponId);
    }

    public List<CouponResponse> findCouponsByMember(Long memberId) {
        return couponRepository.findAllByMemberId(memberId)
                .stream().map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    public int findAllDiscountPriceFromTotalPrice(List<Long> couponIds, Cart cart) {
        final var coupons = couponIds.stream()
                .map(couponRepository::findById)
                .collect(Collectors.toList());
        coupons.stream()
                .map(Coupon::getDiscountConditionId)
                .map(discountPolicyId -> discountPolicyService.findDiscountPriceFromTotalPrice(discountPolicyId, cart));
        return 5000;
    }
}
