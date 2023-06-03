package cart.domain.coupon;

import cart.domain.cart.CartItems;

public class Coupon {

    private final Long id;
    private final CouponInfo couponInfo;
    private final int value;
    private final CouponType type;

    public Coupon(final CouponInfo couponInfo, final int value, final CouponType type) {
        this(null, couponInfo, value, type);
    }

    public Coupon(final Long id, final CouponInfo couponInfo, final int value, final CouponType type) {
        validate(couponInfo, value, type);
        this.id = id;
        this.couponInfo = couponInfo;
        this.value = value;
        this.type = type;
    }

    private void validate(final CouponInfo couponInfo, final int value, final CouponType type) {
        type.validateValue(value, couponInfo.getMinOrderPrice());
    }

    public static Coupon none() {
        return new Coupon(CouponInfo.none(), 0, CouponType.NONE);
    }

    public boolean isApplicable(final CartItems cartItems) {
        return couponInfo.getMinOrderPrice() <= cartItems.calculateTotalProductPrice();
    }

    public int getDiscountPrice(final CartItems cartItems) {
        return type.calculateDiscountPrice(value, cartItems);
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
