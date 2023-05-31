package cart.dto;

import cart.domain.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersResponse {
    
    private List<OrderResponse> orders;
    
    public OrdersResponse(List<OrderResponse> orders) {
        this.orders = orders;
    }
    
    public static OrdersResponse of(List<Order> orders) {
        return new OrdersResponse(
                orders.stream()
                        .map(OrderResponse::of)
                        .collect(Collectors.toList())
        );
    }
    public List<OrderResponse> getOrders() {
        return orders;
    }
}
