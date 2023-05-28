package cart.dto.order;

import cart.domain.order.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersResponse {

    private final List<OrderResponse> orders;

    private OrdersResponse(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static OrdersResponse from(final List<Order> orders) {
        List<OrderResponse> ordersResponse = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        return new OrdersResponse(ordersResponse);
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
