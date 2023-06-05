package cart.coupon.application;

import cart.controller.cart.dto.CouponResponse;
import cart.coupon.Coupon;
import cart.discountpolicy.DiscountPolicy;
import cart.discountpolicy.application.DiscountPolicyService;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.order.OrderCoupon;
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

    public List<OrderCoupon> getOrderCoupons(List<Long> couponIds) {
        return couponIds.stream()
                .map(couponRepository::findById)
                .map(OrderCoupon::from)
                .collect(Collectors.toList());
    }

    public List<DiscountPolicy> findDiscountPoliciesFromCouponIds(List<Long> couponIds) {
        return couponIds.stream()
                .map(couponRepository::findById)
                .map(Coupon::getDiscountPolicy)
                .collect(Collectors.toList());
    }
}
