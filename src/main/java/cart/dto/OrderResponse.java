package cart.dto;

import cart.domain.Order.Order;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long id;
    private final int price;
    private final Timestamp orderDate;
    private final List<OrderItemResponse> orders;

    public OrderResponse(Long id, int price, Timestamp orderDate, List<OrderItemResponse> orders) {
        this.id = id;
        this.price = price;
        this.orderDate = orderDate;
        this.orders = orders;
    }

    public static OrderResponse of(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItem().stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toUnmodifiableList());

        return new OrderResponse(
                order.getId(),
                order.getTotalPrice().price(),
                order.getOrderDate(),
                orderItemResponses
        );
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public List<OrderItemResponse> getOrders() {
        return orders;
    }
}
