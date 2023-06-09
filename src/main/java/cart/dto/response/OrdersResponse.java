package cart.dto.response;

import java.util.List;

public class OrdersResponse {
    private final List<OrderResponse> orders;

    private OrdersResponse(List<OrderResponse> orderResponses) {
        this.orders = orderResponses;
    }

    public static OrdersResponse of(List<OrderResponse> orderResponses) {
        return new OrdersResponse(orderResponses);
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
