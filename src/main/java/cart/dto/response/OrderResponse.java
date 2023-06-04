package cart.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.Order;

public class OrderResponse {
    private final Long id;
    private final List<OrderItemResponse> products;

    private OrderResponse(Long id, List<OrderItemResponse> products) {
        this.id = id;
        this.products = products;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOrderItems()
                .stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toUnmodifiableList())
        );
    }
}
