package cart.dto;

import cart.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderItemResponse> orderItems;

    public OrderResponse(final Long orderId, final List<OrderItemResponse> orderItems) {
        this.orderId = orderId;
        this.orderItems = orderItems;
    }

    public static OrderResponse from(final Order order) {
        List<OrderItemResponse> responses = order.getOrderItems()
                .stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), responses);
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
}
