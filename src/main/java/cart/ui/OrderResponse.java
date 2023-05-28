package cart.ui;

import java.util.List;

public class OrderResponse {
    private final Long orderId;
    private final List<OrderItemResponse> orderItems;

    public OrderResponse(final Long orderId, final List<OrderItemResponse> orderItems) {
        this.orderId = orderId;
        this.orderItems = orderItems;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
}
