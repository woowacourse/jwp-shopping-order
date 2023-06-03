package cart.dto.response;

import cart.domain.coupon.Coupon;

public class CouponResponse {
    private final Long id;
    private final String name;
    private final String discountType;
    private final int minimumPrice;
    private final double discountRate;
    private final int discountAmount;

    private CouponResponse(Long id, String name, String couponType, int minimumPrice, double discountRate, int discountAmount) {
        this.id = id;
        this.name = name;
        this.discountType = couponType;
        this.minimumPrice = minimumPrice;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
    }

    public static CouponResponse from(Coupon coupon) {
        if(coupon.getId() == null){
            return null;
        }
        return new CouponResponse(
                coupon.getId(), coupon.getName(), coupon.getCouponTypes().getCouponTypeName(),
                coupon.getMinimumPrice(), coupon.getDiscountRate(), coupon.getDiscountPrice());
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
}
