package cart.dto.order;

import cart.domain.OrderItem;

import java.util.List;

public class OrderItemsResponse {

    private final long orderId;
    private final List<OrderItem> orderItems;

    public OrderItemsResponse(long orderId, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.orderItems = orderItems;
    }

    public long getOrderId() {
        return orderId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
