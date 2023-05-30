package cart.dto;

public class OrderResponse {
    private final Long orderId;
    private final OrderProductResponse orderProducts;

    public OrderResponse(Long orderId, OrderProductResponse orderProducts) {
        this.orderId = orderId;
        this.orderProducts = orderProducts;
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderProductResponse getOrderProducts() {
        return orderProducts;
    }
}
