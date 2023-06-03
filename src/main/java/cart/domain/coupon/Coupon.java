package cart.domain.coupon;

import cart.domain.cart.CartItems;
import cart.domain.coupon.policy.DiscountPolicy;

public class Coupon {

    private final Long id;
    private final CouponInfo couponInfo;
    private final int value;
    private final CouponType type;

    public Coupon(final CouponInfo couponInfo, final int value, final CouponType type) {
        this(null, couponInfo, value, type);
    }

    public Coupon(final Long id, final CouponInfo couponInfo, final int value, final CouponType type) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public CouponInfo getCouponInfo() {
        return couponInfo;
    }

    public int getValue() {
        return value;
    }

    public CouponType getType() {
        return type;
    }
}
