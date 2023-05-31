package cart.service;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.policy.DiscountPolicy;
import cart.domain.coupon.policy.DiscountPolicyType;
import cart.dto.CouponSaveRequest;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Long save(final CouponSaveRequest request) {
        final DiscountPolicy discountPolicy = DiscountPolicyType.findDiscountPolicy(request.getDiscountPolicyType(),
                request.getDiscountValue());
        final Coupon coupon = new Coupon(request.getName(), discountPolicy, request.getMinimumPrice());

        return couponRepository.saveCoupon(coupon).getId();
    }
}
