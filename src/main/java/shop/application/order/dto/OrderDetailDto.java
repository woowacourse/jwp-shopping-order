package shop.application.order.dto;

import shop.domain.coupon.Coupon;
import shop.domain.order.Order;

public class OrderDetailDto {
    private final Order order;
    private final Coupon coupon;

    public OrderDetailDto(Order order,Coupon coupon) {
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
