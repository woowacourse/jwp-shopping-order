package cart.dto;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.Coupon;

public class CouponResponse {

    private final Long couponId;
    private final String name;
    private final DiscountPolicyResponse discount;

    public CouponResponse(Long couponId, String name, DiscountPolicyResponse discount) {
        this.couponId = couponId;
        this.name = name;
        this.discount = discount;
    }

    public static CouponResponse of(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                "쿠폰 이름",
                DiscountPolicyResponse.of(coupon.getDiscountPolicy())
                        .withAmount(coupon.getAmount())
        );
    }

    public static List<CouponResponse> of(List<Coupon> coupons) {
        return coupons.stream()
                .map(CouponResponse::of)
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
