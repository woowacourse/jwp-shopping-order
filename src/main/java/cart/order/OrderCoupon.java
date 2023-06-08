package cart.order;

import cart.coupon.Coupon;

public class OrderCoupon {
    private final Long couponId;
    private final String couponName;

    private OrderCoupon(Long couponId, String couponName) {
        this.couponId = couponId;
        this.couponName = couponName;
    }

    public static OrderCoupon from(Coupon coupon) {
        return new OrderCoupon(coupon.getId(), coupon.getName());
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }
}
