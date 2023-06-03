package shop.application.order.dto;

import shop.application.coupon.dto.CouponDto;
import shop.domain.order.OrderDetail;

public class OrderDetailDto {
    private final OrderDto order;
    private final CouponDto coupon;

    private OrderDetailDto(OrderDto order, CouponDto coupon) {
        this.order = order;
        this.coupon = coupon;
    }

    public static OrderDetailDto of(OrderDetail orderDetail) {
        return new OrderDetailDto(
                OrderDto.of(orderDetail.getOrder()),
                CouponDto.of(orderDetail.getCoupon())
        );
    }

    public OrderDto getOrder() {
        return order;
    }

    public CouponDto getCoupon() {
        return coupon;
    }
}
