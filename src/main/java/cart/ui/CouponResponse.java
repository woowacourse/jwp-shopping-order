package cart.ui;

public class CouponResponse {
    private final String couponName;
    private final Integer discountPercent;
    private final Integer minAmount;

    public CouponResponse(final String couponName, final Integer discountPercent, final Integer minAmount) {
        this.couponName = couponName;
        this.discountPercent = discountPercent;
        this.minAmount = minAmount;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public Integer getMinAmount() {
        return minAmount;
    }
}
