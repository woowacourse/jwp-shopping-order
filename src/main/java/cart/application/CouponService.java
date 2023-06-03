package cart.application;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import cart.domain.discountpolicy.DiscountType;
import cart.dto.CouponIssueRequest;
import cart.dto.CouponResponse;
import cart.dto.CouponsResponse;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final DiscountPolicyProvider discountPolicyProvider;

    public CouponService(
            CouponRepository couponRepository,
            MemberCouponRepository memberCouponRepository,
            DiscountPolicyProvider discountPolicyProvider
    ) {
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
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

    @Transactional
    public Long issueCouponToMember(Member member, CouponIssueRequest couponIssueRequest) {
        Long couponId = couponIssueRequest.getCouponId();
        Coupon couponToIssue = couponRepository.findById(couponId);

        return memberCouponRepository.insert(member, couponToIssue);
    }
}
