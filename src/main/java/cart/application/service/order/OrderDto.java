package cart.application.service.order;

import cart.application.service.coupon.CouponResultDto;
import cart.domain.order.Order;

import java.util.List;

public class OrderDto {
    private final Long orderId;
    private final List<OrderItemDto> orderItems;
    private final List<CouponResultDto> usedCoupons;
    private final int usedPoint;
    private final int paymentPrice;

    public OrderDto(final Long orderId, final List<OrderItemDto> orderItems, final List<CouponResultDto> usedCoupons, final int usedPoint, final int paymentPrice) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.usedCoupons = usedCoupons;
        this.usedPoint = usedPoint;
        this.paymentPrice = paymentPrice;
    }

    public static OrderDto of(final Order order) {
//        OrderItemDto.of(order.getOrderItems())
        return null;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public List<CouponResultDto> getUsedCoupons() {
        return usedCoupons;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }
}
