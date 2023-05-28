package cart.dto.order;

import java.util.List;

public class OrdersResponse {

    private final List<OrderResponse> orders;

    public OrdersResponse(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
