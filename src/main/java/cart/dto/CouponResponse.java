package cart.dto;

import cart.domain.coupon.Coupon;

public class CouponResponse {
    private final Long id;
    private final String name;
    private final String discountPolicyName;
    private final int discountValue;

    public CouponResponse(Long id, String name, String discountPolicyName, int discountValue) {
        this.id = id;
        this.name = name;
        this.discountPolicyName = discountPolicyName;
        this.discountValue = discountValue;
    }

    public static CouponResponse of(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountPolicyName(),
                coupon.getDiscountValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscountPolicyName() {
        return discountPolicyName;
    }

    public int getDiscountValue() {
        return discountValue;
    }
}
