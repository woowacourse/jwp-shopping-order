package cart.domain;

public class Coupon {
    private final long id;
    private final String couponName;
    private final int discountPercent;
    private final int minAmount;

    public Coupon(final Long id, final String couponName, final Integer discountPercent, final Integer minAmount) {
        this.id = id;
        this.couponName = couponName;
        this.discountPercent = discountPercent;
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

    public Integer getMinAmount() {
        return minAmount;
    }
}
