package cart.dto.response;

import java.beans.ConstructorProperties;
import java.util.List;

public class OrdersResponse {

    private final List<OrderResponse> orders;

    @ConstructorProperties(value = {"orders"})
    public OrdersResponse(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
