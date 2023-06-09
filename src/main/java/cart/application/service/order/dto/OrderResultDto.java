package cart.application.service.order.dto;

import cart.application.service.coupon.dto.CouponResultDto;
import cart.domain.order.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResultDto {
    private final Long orderId;
    private final List<OrderItemDto> orderItems;
    private final List<CouponResultDto> usedCoupons;

    private final int usedPoint;
    private final int paymentPrice;
    private final String createAt;

    public OrderResultDto(final Long orderId, final List<OrderItemDto> orderItems, final List<CouponResultDto> usedCoupons, final int usedPoint, final int paymentPrice, final String createAt) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.usedCoupons = usedCoupons;
        this.usedPoint = usedPoint;
        this.paymentPrice = paymentPrice;
        this.createAt = createAt;
    }

    public static OrderResultDto from(final Order order) {
        return new OrderResultDto(
                order.getId(),
                OrderItemDto.from(order.getOrderItems(), order.getPaymentPrice()),
                CouponResultDto.from(order.getCoupons()),
                order.getPoint().getPoint(),
                order.getPaymentPrice(),
                order.getCreateAt().toString());
    }


    public static List<OrderResultDto> from(final List<Order> orders) {
        return orders.stream()
                .map(order -> new OrderResultDto(
                        order.getId(),
                        OrderItemDto.from(order.getOrderItems(), order.getPaymentPrice()),
                        CouponResultDto.from(order.getCoupons()),
                        order.getPoint().getPoint(),
                        order.getPaymentPrice(),
                        order.getCreateAt().toString()
                )).collect(Collectors.toUnmodifiableList());
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

    public String getCreateAt() {
        return createAt;
    }
}
