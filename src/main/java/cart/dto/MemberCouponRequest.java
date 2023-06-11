package cart.dto;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.MemberCoupon;

public class MemberCouponRequest {

    private Long couponId;
    private String name;
    private DiscountPolicyRequest discount;

    public MemberCouponRequest(Long couponId, String name, DiscountPolicyRequest discount) {
        this.couponId = couponId;
        this.name = name;
        this.discount = discount;
    }

    public static MemberCouponRequest of(MemberCoupon memberCoupon) {
        return new MemberCouponRequest(
                memberCoupon.getId(),
                "쿠폰 이름",
                DiscountPolicyRequest.of(memberCoupon.getCoupon().getDiscountPolicy())
                        .withAmount(memberCoupon.getCoupon().getAmount())
        );
    }

    public static List<MemberCouponRequest> of(List<MemberCoupon> memberCoupons) {
        return memberCoupons.stream()
                .map(MemberCouponRequest::of)
                .collect(Collectors.toList());
    }
    
    public Long getCouponId() {
        return couponId;
    }

    public String getName() {
        return name;
    }

    public DiscountPolicyRequest getDiscount() {
        return discount;
    }
}
