package cart.dto.response;

import cart.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersResponse {

    private List<OrderResponse> orders;

    public OrdersResponse() {
    }

    private OrdersResponse(List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static OrdersResponse of(List<Order> orders) {
        List<OrderResponse> ordersResponse = orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
        return new OrdersResponse(ordersResponse);
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
