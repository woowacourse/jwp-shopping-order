package cart.dto;

import cart.domain.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long orderId;
    private List<OrderItemResponse> products;

    private OrderResponse(final Long orderId, final List<OrderItemResponse> products) {
        this.orderId = orderId;
        this.products = products;
    }

    public static List<OrderResponse> from(List<Order> orders) {
        return orders.stream()
                .map(order -> new OrderResponse(order.getId(), OrderItemResponse.from(order.getOrderItems())))
                .collect(Collectors.toList());
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }
}
