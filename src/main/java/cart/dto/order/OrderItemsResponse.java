package cart.dto.order;

import cart.domain.OrderItem;

import java.util.List;

public class OrderItemsResponse {

    private final long orderId;
    private final List<OrderItem> orderProducts;

    public OrderItemsResponse(long orderId, List<OrderItem> orderProducts) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
    }

    public long getOrderId() {
        return orderId;
    }

    public List<OrderItem> getOrderProducts() {
        return orderProducts;
    }
}
