package cart.dto;

import java.util.List;

public class OrderResponses {
    private final List<OrderResponse> orders;

    public OrderResponses(List<OrderResponse> orders) {
        this.orders = orders;
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
