package cart.dto.order;

import cart.domain.Order;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    @JsonProperty("orderId")
    private Long id;
    private List<OrderItemResponse> orderItems;

    private OrderResponse() {
    }

    public OrderResponse(final Long id, final List<OrderItemResponse> orderItems) {
        this.id = id;
        this.orderItems = orderItems;
    }

    public static OrderResponse from(final Order order) {
        List<OrderItemResponse> responses = order.getOrderItems()
                .stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), responses);
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
}
