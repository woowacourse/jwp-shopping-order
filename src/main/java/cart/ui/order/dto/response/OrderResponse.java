package cart.ui.order.dto.response;

import cart.application.service.order.dto.OrderResultDto;
import cart.ui.coupon.dto.CouponResponse;

import java.util.List;

public class OrderResponse {

    private Long orderId;
    private List<OrderItemResponse> orderItems;
    private List<CouponResponse> usedCoupons;
    private int usedPoint;
    private int paymentPrice;
    private String createAt;

    public OrderResponse() {
    }

    public OrderResponse(
            final Long orderId,
            final List<OrderItemResponse> orderItems,
            final List<CouponResponse> usedCoupons,
            final int usedPoint,
            final int paymentPrice,
            final String createAt
    ) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.usedCoupons = usedCoupons;
        this.usedPoint = usedPoint;
        this.paymentPrice = paymentPrice;
        this.createAt = createAt;
    }

    public static OrderResponse from(final OrderResultDto orderResultDto) {
        return new OrderResponse(
                orderResultDto.getOrderId(),
                OrderItemResponse.from(orderResultDto.getOrderItems()),
                CouponResponse.from(orderResultDto.getUsedCoupons()),
                orderResultDto.getUsedPoint(),
                orderResultDto.getPaymentPrice(),
                orderResultDto.getCreateAt()
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

    public String getCreateAt() {
        return createAt;
    }
}
