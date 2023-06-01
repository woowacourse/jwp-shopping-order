package cart.dto;

import java.util.List;

public class OrdersResponse {

    final private List<OrderResponse> orders;

    public OrdersResponse(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
