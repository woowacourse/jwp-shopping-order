package cart.dto.order;

import cart.domain.OrderProduct;

import java.util.List;

public class OrderItemsResponse {

    private final long orderId;
    private final List<OrderProduct> orderProducts;

    public OrderItemsResponse(long orderId, List<OrderProduct> orderProducts) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
    }

    public long getOrderId() {
        return orderId;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }
}
