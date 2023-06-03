package shop.domain.order;

import shop.domain.coupon.Coupon;

public class OrderDetail {
    private final Order order;
    private final Coupon coupon;

    public OrderDetail(Order order, Coupon coupon) {
        this.order = order;
        this.coupon = coupon;
    }

    public Order getOrder() {
        return order;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}
