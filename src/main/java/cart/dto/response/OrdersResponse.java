package cart.dto.response;

import java.util.List;

public class OrdersResponse {

    private List<OrderResponse> orders;

    public OrdersResponse() {
    }

    public OrdersResponse(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
