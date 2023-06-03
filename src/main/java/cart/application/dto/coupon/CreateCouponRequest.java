package cart.application.dto.coupon;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponInfo;
import cart.domain.coupon.CouponType;

public class CreateCouponRequest {

    private String name;
    private int minOrderPrice;
    private int maxDiscountPrice;
    private CouponType type;
    private int value;

    public CreateCouponRequest() {
    }

    public CreateCouponRequest(final String name, final int minOrderPrice, final int maxDiscountPrice,
            final CouponType type, final int value) {
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
        this.type = type;
        this.value = value;
    }

    public Coupon toDomain() {
        CouponInfo couponInfo = new CouponInfo(name, minOrderPrice, maxDiscountPrice);
        return new Coupon(couponInfo, value, type);
    }

    public String getName() {
        return name;
    }

    public int getMinOrderPrice() {
        return minOrderPrice;
    }

    public int getMaxDiscountPrice() {
        return maxDiscountPrice;
    }

    public CouponType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
