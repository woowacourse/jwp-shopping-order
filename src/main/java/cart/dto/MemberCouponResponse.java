package cart.dto;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.MemberCoupon;

public class MemberCouponResponse {

    private Long couponId;
    private String name;
    private DiscountPolicyResponse discount;

    public MemberCouponResponse(Long couponId, String name, DiscountPolicyResponse discount) {
        this.couponId = couponId;
        this.name = name;
        this.discount = discount;
    }

    public static MemberCouponResponse of(MemberCoupon memberCoupon) {
        return new MemberCouponResponse(
                memberCoupon.getId(),
                "쿠폰 이름",
                DiscountPolicyResponse.of(memberCoupon.getCoupon().getDiscountPolicy())
                        .withAmount(memberCoupon.getCoupon().getAmount())
        );
    }

    public static List<MemberCouponResponse> of(List<MemberCoupon> memberCoupons) {
        return memberCoupons.stream()
                .map(MemberCouponResponse::of)
                .collect(Collectors.toList());
    }
    
    public Long getCouponId() {
        return couponId;
    }

    public String getName() {
        return name;
    }

    public DiscountPolicyResponse getDiscount() {
        return discount;
    }
}
