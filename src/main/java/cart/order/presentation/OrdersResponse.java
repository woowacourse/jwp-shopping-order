package cart.order.presentation;

import cart.order.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersResponse {
    private List<OrderResponse> orders;

    public OrdersResponse() {
    }

    private OrdersResponse(List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static OrdersResponse from(List<Order> orders) {
        final var orderResponses = orders
                .stream().map(OrderResponse::from)
                .collect(Collectors.toList());
        return new OrdersResponse(orderResponses);
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
