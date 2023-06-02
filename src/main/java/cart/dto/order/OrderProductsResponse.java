package cart.dto.order;

import java.util.List;

public class OrderProductsResponse {

    private final long orderId;
    private final List<OrderProductResponse> orderProducts;

    public OrderProductsResponse(long orderId, List<OrderProductResponse> orderProductResponses) {
        this.orderId = orderId;
        this.orderProducts = orderProductResponses;
    }

    public long getOrderId() {
        return orderId;
    }

    public List<OrderProductResponse> getOrderProducts() {
        return orderProducts;
    }
}
