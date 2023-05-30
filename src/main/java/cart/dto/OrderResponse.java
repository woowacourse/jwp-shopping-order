package cart.dto;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.Order;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderItemResponse> orderItems;

    public OrderResponse(Long orderId, List<OrderItemResponse> orderItems) {
        this.orderId = orderId;
        this.orderItems = orderItems;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(
                order.getId(),
                OrderItemResponse.of(order.getOrderItems())
        );
    }

    public static List<OrderResponse> of(List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
}
