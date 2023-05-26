package cart.dto;

import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final String createAt;
    private final List<OrderItemResponse> orderItems;
    private final Long totalPrice;
    private final Long usedPoint;
    private final Long earnedPoint;

    public OrderResponse(final Long orderId, final String createAt, final List<OrderItemResponse> orderItems, final Long totalPrice, final Long usedPoint, final Long earnedPoint) {
        this.orderId = orderId;
        this.createAt = createAt;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.earnedPoint = earnedPoint;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }

    public Long getEarnedPoint() {
        return earnedPoint;
    }
}
