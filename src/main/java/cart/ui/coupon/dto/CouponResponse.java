package cart.ui.coupon.dto;

public class CouponResponse {

    private final Long id;
    private final String couponName;
    private final Integer discountPercent;
    private final Integer discountAmount;
    private final Integer minAmount;

    public CouponResponse(final Long id, final String couponName, final Integer discountPercent, final Integer discountAmount, final Integer minAmount) {
        this.id = id;
        this.couponName = couponName;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.minAmount = minAmount;
    }

    public Long getId() {
        return id;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

}
