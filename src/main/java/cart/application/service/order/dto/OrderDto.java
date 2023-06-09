package cart.application.service.order.dto;

import cart.application.service.coupon.dto.CouponResultDto;
import cart.domain.order.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    private final Long orderId;
    private final List<OrderItemDto> orderItems;
    private final List<CouponResultDto> usedCoupons;
    private final int usedPoint;
    private final int paymentPrice;
    private final String createdAt;

    public OrderDto(final Long orderId, final List<OrderItemDto> orderItems, final List<CouponResultDto> usedCoupons,
                    final int usedPoint, final int paymentPrice, String createdAt) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.usedCoupons = usedCoupons;
        this.usedPoint = usedPoint;
        this.paymentPrice = paymentPrice;
        this.createdAt = createdAt;
    }

    public static OrderDto of(final Order order) {
        return new OrderDto(order.getId(),
                order.getOrderItems().stream()
                        .map(OrderItemDto::of)
                        .collect(Collectors.toUnmodifiableList()),
                order.getCoupons().stream()
                        .map(CouponResultDto::from)
                        .collect(Collectors.toUnmodifiableList()),
                order.getPoint(),
                order.getPaymentPrice(),
                order.getCreatedAt());
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

    public String getCreatedAt() {
        return createdAt;
    }

}
