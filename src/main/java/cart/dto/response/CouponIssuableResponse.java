package cart.dto.response;

import cart.domain.coupon.Coupon;

public class CouponIssuableResponse {
    private final Long id;
    private final String name;
    private final String discountType;
    private final int minimumPrice;
    private final double discountRate;
    private final int discountAmount;
    private final boolean issuable;

    private CouponIssuableResponse(Long id, String name, String couponType, int minimumPrice, double discountRate, int discountAmount, boolean issuable) {
        this.id = id;
        this.name = name;
        this.discountType = couponType;
        this.minimumPrice = minimumPrice;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.issuable = issuable;
    }

    public static CouponIssuableResponse of(Coupon it, boolean isIssuable) {
        return new CouponIssuableResponse(it.getId(), it.getName(),
                it.getCouponTypes().getCouponTypeName(),
                it.getMinimumPrice(), it.getDiscountRate(), it.getDiscountPrice(),
                isIssuable);
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscountType() {
        return discountType;
    }

    public int getMinimumPrice() {
        return minimumPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public boolean isIssuable() {
        return issuable;
    }
}
