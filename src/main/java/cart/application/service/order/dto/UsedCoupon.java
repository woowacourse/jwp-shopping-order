package cart.application.service.order.dto;

import cart.domain.coupon.Coupon;

public class UsedCoupon {
    private final long id;
    private final String couponName;
    private final int discountPercent;
    private final int discountAmount;
    private final int minAmount;

    public UsedCoupon(long id, String couponName, int discountPercent, int discountAmount, int minAmount) {
        this.id = id;
        this.couponName = couponName;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.minAmount = minAmount;
    }

    public static UsedCoupon from(Coupon coupon) {
        return new UsedCoupon(
                coupon.getId(),
                coupon.getCouponName(),
                coupon.getDiscountPercent(),
                coupon.getDiscountAmount(),
                coupon.getMinAmount()
        );
    }

    public long getId() {
        return id;
    }

    public String getCouponName() {
        return couponName;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public int getMinAmount() {
        return minAmount;
    }
}
