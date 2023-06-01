package cart.dto;

import cart.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersResponse {

    private List<OrderResponse> ordersResponse;

    public OrdersResponse() {
    }

    private OrdersResponse(List<OrderResponse> ordersResponse) {
        this.ordersResponse = ordersResponse;
    }

    public static OrdersResponse of(List<Order> orders) {
        List<OrderResponse> ordersResponse = orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
        return new OrdersResponse(ordersResponse);
    }

    public List<OrderResponse> getOrdersResponse() {
        return ordersResponse;
    }
}
