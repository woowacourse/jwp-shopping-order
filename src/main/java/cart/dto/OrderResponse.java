package cart.dto;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;

public class OrderResponse {

    private final Long orderId;
    private final Integer deliveryFee;
    private final Integer total;
    private final List<OrderItemResponse> orderItems;

    public OrderResponse(Long orderId, Integer deliveryFee, Integer total, List<OrderItemResponse> orderItems) {
        this.orderId = orderId;
        this.deliveryFee = deliveryFee;
        this.total = total;
        this.orderItems = orderItems;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(
                order.getId(),
                3000,
                order.getOrderItems().stream()
                        .map(OrderItem::getTotal)
                        .reduce(Money::plus)
                        .orElse(Money.ZERO).getValue(),
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

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public Integer getTotal() {
        return total;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
}
