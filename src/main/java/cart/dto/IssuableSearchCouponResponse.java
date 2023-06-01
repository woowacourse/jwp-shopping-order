package cart.dto;

import cart.domain.coupon.Coupon;

public class IssuableSearchCouponResponse {
    private final Long id;
    private final String name;
    private final String discountType;
    private final Double discountRate;
    private final Integer discountAmount;
    private final Integer minimumPrice;
    private final Boolean issuable;

    public IssuableSearchCouponResponse(Long id, String name, String discountType, Double discountRate, Integer discountAmount, Integer minimumPrice, Boolean issuable) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
        this.issuable = issuable;
    }

    public static IssuableSearchCouponResponse of(Coupon coupon, boolean issuable) {
        return new IssuableSearchCouponResponse(coupon.getId(), coupon.getName(), coupon.getDiscountType().getName(), coupon.getDiscountPercent(), coupon.getDiscountAmount(), coupon.getMinimumPrice(), issuable);
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

    public Double getDiscountRate() {
        return discountRate;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Integer getMinimumPrice() {
        return minimumPrice;
    }

    public Boolean getIssuable() {
        return issuable;
    }
}
