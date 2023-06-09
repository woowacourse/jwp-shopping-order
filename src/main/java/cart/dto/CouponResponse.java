package cart.dto;

import cart.domain.coupon.Coupon;

public class CouponResponse {
    private final Long id;
    private final String name;
    private final String discountType;
    private final Double discountRate;
    private final Integer discountAmount;
    private final Integer minimumPrice;

    public CouponResponse(Long id, String name, String discountType, Double discountRate, Integer discountAmount, Integer minimumPrice) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.minimumPrice = minimumPrice;
    }

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountType().getName(),
                coupon.getDiscountPercent(),
                coupon.getDiscountAmount(),
                coupon.getMinimumPrice()
        );
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
}
