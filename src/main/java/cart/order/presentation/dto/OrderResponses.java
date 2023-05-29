package cart.order.presentation.dto;

import cart.order.domain.Order;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderResponses {

    private final List<OrderResponse> orders;

    public OrderResponses(List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static OrderResponses from(Map<Order, Integer> orders) {
        List<OrderResponse> orderResponses = orders.entrySet().stream()
                .map(entry -> OrderResponse.from(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return new OrderResponses(orderResponses);
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}
