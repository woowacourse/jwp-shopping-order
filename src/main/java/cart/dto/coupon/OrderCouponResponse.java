package cart.dto.coupon;

import cart.domain.coupon.Coupon;

public class OrderCouponResponse {

    private String name;
    private int discountRate;

    public OrderCouponResponse() {
    }

    public OrderCouponResponse(final String name, final int discountRate) {
        this.name = name;
        this.discountRate = discountRate;
    }

    public OrderCouponResponse(final Coupon coupon) {
        this.name = coupon.getName();
        this.discountRate = coupon.getDiscountRate();
    }

    public String getName() {
        return name;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
