package cart.ui.order.dto.response;

import cart.application.service.order.dto.OrderResultDto;
import cart.application.service.order.dto.OrderedItemResult;
import cart.application.service.order.dto.UsedCoupon;

import java.util.List;

public class OrderResponse {

    private Long orderId;
    private List<OrderedItemResult> orderItems;
    private List<UsedCoupon> usedCoupons;
    private int usedPoint;
    private int paymentPrice;

    public OrderResponse() {
    }

    public OrderResponse(Long orderId, List<OrderedItemResult> orderItems, List<UsedCoupon> usedCoupons, int usedPoint, int paymentPrice) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.usedCoupons = usedCoupons;
        this.usedPoint = usedPoint;
        this.paymentPrice = paymentPrice;
    }

    public static OrderResponse from(OrderResultDto orderResultDto) {
        return new OrderResponse(
                orderResultDto.getOrderId(),
                orderResultDto.getOrderedItems(),
                orderResultDto.getUsedCoupons(),
                orderResultDto.getUsedPoint(),
                orderResultDto.getPaymentPrice()
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderedItemResult> getOrderItems() {
        return orderItems;
    }

    public List<UsedCoupon> getUsedCoupons() {
        return usedCoupons;
    }

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }

}
