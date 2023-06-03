package cart.ui.order.dto;

import cart.application.service.order.dto.OrderDto;
import cart.ui.coupon.dto.CouponResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long orderId;
    private List<OrderItemResponse> orderItems;
    private List<CouponResponse> usedCoupons;
    private int usedPoint;
    private int paymentPrice;
    private String createdAt;

    public OrderResponse(String createdAt) {
        this.createdAt = createdAt;
    }

    public OrderResponse(final Long orderId,
                         final List<OrderItemResponse> orderItems,
                         final List<CouponResponse> usedCoupons,
                         final int usedPoint,
                         final int paymentPrice,
                         String createdAt) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.usedCoupons = usedCoupons;
        this.usedPoint = usedPoint;
        this.paymentPrice = paymentPrice;
        this.createdAt = createdAt;
    }

    public static OrderResponse from(final OrderDto orderDto) {
        return new OrderResponse(
                orderDto.getOrderId(),
                OrderItemResponse.from(orderDto.getOrderItems()),
                orderDto.getUsedCoupons().stream().map(CouponResponse::from).collect(Collectors.toUnmodifiableList()),
                orderDto.getUsedPoint(),
                orderDto.getPaymentPrice(),
                orderDto.getCreatedAt()
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public List<CouponResponse> getUsedCoupons() {
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
