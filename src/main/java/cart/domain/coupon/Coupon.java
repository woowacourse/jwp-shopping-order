package cart.domain.coupon;

import cart.domain.cart.CartItems;
import cart.domain.coupon.policy.DiscountPolicy;

public class Coupon {

    private final CouponInfo couponInfo;
    private final int value;
    private final CouponType type;

    public Coupon(final CouponInfo couponInfo, final int value, final CouponType type) {
        this.couponInfo = couponInfo;
        this.value = value;
        this.type = type;
    }

    public boolean isApplicable(final CartItems cartItems) {
        return couponInfo.getMinOrderPrice() <= cartItems.calculateTotalProductPrice();
    }

    public int getDiscountPrice(final CartItems cartItems) {
        DiscountPolicy discountPolicy = type.getDiscountPolicy();
        return discountPolicy.calculateDiscountPrice(value, cartItems);
    }

    public CouponInfo getCouponInfo() {
        return couponInfo;
    }
}
