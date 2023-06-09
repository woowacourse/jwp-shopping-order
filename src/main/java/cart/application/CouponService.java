package cart.application;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.discountpolicy.DiscountPolicy;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import cart.domain.discountpolicy.DiscountType;
import cart.dto.apidatamapper.DiscountAmountMapper;
import cart.dto.apidatamapper.DiscountTypeMapper;
import cart.dto.request.CouponIssueRequest;
import cart.dto.request.CouponRequest;
import cart.dto.response.CouponResponse;
import cart.dto.response.CouponsResponse;
import cart.exception.CouponException;
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
    public CouponResponse makeCoupon(final CouponRequest couponRequest) {
        Coupon coupon = requestToCoupon(couponRequest);
        Long insertedId = couponRepository.insert(coupon);

        Coupon insertedCoupon = couponRepository.findById(insertedId);
        return couponToResponse(insertedCoupon);
    }

    private Coupon requestToCoupon(final CouponRequest couponRequest) {
        DiscountType discountType = DiscountTypeMapper.apiBodyStringToDomain(couponRequest.getType());
        DiscountPolicy discountPolicy = discountPolicyProvider.getByType(discountType);
        double domainValue = DiscountAmountMapper.apiBodyAmountToDomainValue(discountType, couponRequest.getAmount());
        return new Coupon(couponRequest.getName(), discountPolicy, domainValue);
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

        validateIsIssuable(member, couponId);

        return memberCouponRepository.insert(member, couponToIssue);
    }

    private void validateIsIssuable(final Member member, final Long couponId) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findAllByMemberId(member.getId());
        boolean isContainSameUnusedCoupon = memberCoupons.stream()
                .anyMatch(coupon -> (!coupon.isUsed()) && coupon.getCouponId().equals(couponId));

        if (isContainSameUnusedCoupon) {
            throw new CouponException.AlreadHaveSameCouponException();
        }
    }

    @Transactional
    public CouponsResponse getAllUnusedCoupons(Member member) {
        List<MemberCoupon> allCoupons = memberCouponRepository.findAllByMemberId(member.getId());

        List<CouponResponse> unusedCouponResponses = allCoupons.stream()
                .filter(coupon -> !coupon.isUsed())
                .map(this::memberCouponToResponse)
                .collect(Collectors.toList());
        return new CouponsResponse(unusedCouponResponses);
    }

    private CouponResponse memberCouponToResponse(MemberCoupon coupon) {
        DiscountType discountType = discountPolicyProvider.getDiscountType(coupon.getDiscountPolicy());
        return CouponResponse.of(coupon, discountType);
    }

}
