package cart.application;

import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import cart.domain.discountpolicy.DiscountType;
import cart.dto.CouponResponse;
import cart.dto.CouponsResponse;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final DiscountPolicyProvider discountPolicyProvider;

    public CouponService(CouponRepository couponRepository, DiscountPolicyProvider discountPolicyProvider) {
        this.couponRepository = couponRepository;
        this.discountPolicyProvider = discountPolicyProvider;
    }

    @Transactional
    public CouponsResponse getAllCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        List<CouponResponse> couponResponses = coupons.stream()
                .map(this::couponToResponse)
                .collect(Collectors.toUnmodifiableList());

        return new CouponsResponse(couponResponses);
    }

    private CouponResponse couponToResponse(Coupon coupon) {
        DiscountType discountType = discountPolicyProvider.getDiscountType(coupon.getDiscountPolicy());
        return CouponResponse.of(coupon, discountType);
    }
}
