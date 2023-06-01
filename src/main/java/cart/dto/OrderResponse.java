package cart.dto;

import java.util.List;

public class OrderResponse {
    private final Long orderId;
    private final List<OrderProductResponse> orderProducts;

    public OrderResponse(Long orderId, List<OrderProductResponse> orderProducts) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderProductResponse> getOrderProducts() {
        return orderProducts;
    }
}
