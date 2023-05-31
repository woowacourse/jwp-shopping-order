package cart.dto;

import cart.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderListResponse {

    private final List<OrderResponse> orders;

    private OrderListResponse(final List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static OrderListResponse of(final List<Order> orders) {
        return new OrderListResponse(orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList()));
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
