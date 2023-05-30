package cart.step2.coupon.presentation.dto;

import cart.step2.coupon.domain.Coupon;
import cart.step2.coupontype.domain.CouponType;

public class CouponResponse {

    private final Long id;
    private final String name;
    private final int discountAmount;
    private final String description;
    private final boolean isUsed;

    public CouponResponse(final Coupon coupon, final CouponType couponType) {
        this.id = coupon.getId();
        this.name = couponType.getName();
        this.discountAmount = couponType.getDiscountAmount();
        this.description = couponType.getDescription();
        this.isUsed = judgeTrueOrFalse(coupon.getUsageStatus());
    }

    private boolean judgeTrueOrFalse(final String usageStatus) {
        return usageStatus.equals("Y");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
