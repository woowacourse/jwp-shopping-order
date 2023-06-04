package cart.dto.response;

import cart.domain.Coupon;
import cart.domain.CouponType;

public class CouponResponse {
    private final Long id;
    private final String name;
    private final Integer minOrderPrice;
    private final Integer maxDiscountPrice;
    private final CouponType couponType;
    private final Integer discountAmount;
    private final Double discountPercentage;

    private CouponResponse(Long id, String name, Integer minOrderPrice, Integer maxDiscountPrice, CouponType couponType,
        Integer discountAmount, Double discountPercentage) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
        this.couponType = couponType;
        this.discountAmount = discountAmount;
        this.discountPercentage = discountPercentage;
    }

    public static CouponResponse of(Coupon coupon) {
        return new CouponResponse(
            coupon.getId(),
            coupon.getName(),
            coupon.getMinOrderPrice(),
            coupon.getMaxDiscountPrice(),
            coupon.getType(),
            coupon.getDiscountAmount(),
            coupon.getDiscountPercentage()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getMinOrderPrice() {
        return minOrderPrice;
    }

    public Integer getMaxDiscountPrice() {
        return maxDiscountPrice;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }
}
