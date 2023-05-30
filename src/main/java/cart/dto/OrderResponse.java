package cart.dto;

import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderItemResponse> orderItems;

    public OrderResponse(Long orderId, List<OrderItemResponse> orderItems) {
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
