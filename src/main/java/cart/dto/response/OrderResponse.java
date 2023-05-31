package cart.dto.response;

import java.util.List;

public class OrderResponse {

    private Long id;
    private List<OrderItemResponse> orderItems;

    public OrderResponse() {
    }

    public OrderResponse(Long id, List<OrderItemResponse> orderItems) {
        this.id = id;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
}
