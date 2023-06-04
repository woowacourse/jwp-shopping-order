package cart.dto.response;

import java.util.List;

public class OrdersResponse {
    private final List<OrderResponse> orderResponses;

    private OrdersResponse(List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public static OrdersResponse of(List<OrderResponse> orderResponses) {
        return new OrdersResponse(orderResponses);
    }

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }
}
