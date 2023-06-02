package cart.dto;

import cart.domain.Order;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderItemResponse> orderItems;

    private OrderResponse(Long orderId, List<OrderItemResponse> orderItems) {
        this.orderId = orderId;
        this.orderItems = orderItems;
    }

    public static OrderResponse of(Order order) {
        List<OrderItemResponse> orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::of)
                .collect(toList());
        return new OrderResponse(order.getId(), orderItems);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(orderItems, that.orderItems);
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderItems);
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderId=" + orderId +
                ", orderItems=" + orderItems +
                '}';
    }
}
