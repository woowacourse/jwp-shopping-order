package cart.ui.order.dto;

import cart.ui.coupon.dto.CouponResponse;

import java.util.List;

public class OrderResponse {

    private Long orderId;
    private List<OrderItemResponse> orderItems;
    private List<CouponResponse> usedCoupons;
    private int usedPoint;
    private int paymentPrice;

    public OrderResponse() {
    }

    public OrderResponse(final Long orderId, final List<OrderItemResponse> orderItems, final List<CouponResponse> usedCoupons, final int usedPoint, final int paymentPrice) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.usedCoupons = usedCoupons;
        this.usedPoint = usedPoint;
        this.paymentPrice = paymentPrice;
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

}
