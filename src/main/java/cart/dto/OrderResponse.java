package cart.dto;

import cart.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long id;
    private final List<OrderItemResponse> orderItems;
    private final int totalPrice;
    private final int payPrice;
    private final int earnedPoints;
    private final int usedPoints;
    private final String orderDate;

    private OrderResponse(final Long id, final List<OrderItemResponse> orderItems, final int totalPrice, final int payPrice, final int earnedPoints, final int usedPoints, final String orderDate) {
        this.id = id;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.payPrice = payPrice;
        this.earnedPoints = earnedPoints;
        this.usedPoints = usedPoints;
        this.orderDate = orderDate;
    }

    public static OrderResponse of(final Order order) {
        final List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(),
                orderItemResponses,
                order.getTotalPrice(),
                order.getPayPrice(),
                order.getEarnedPoints(),
                order.getUsedPoints(),
                order.getOrderDate());
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPayPrice() {
        return payPrice;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
