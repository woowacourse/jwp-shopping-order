package cart.dto.order;

import cart.domain.order.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long orderId;
    private int deliveryFee;
    private int total;
    private List<OrderItemResponse> orderItems;

    private OrderResponse() {
    }

    public OrderResponse(final Long orderId, final int deliveryFee, final int total, final List<OrderItemResponse> orderItems) {
        this.orderId = orderId;
        this.deliveryFee = deliveryFee;
        this.total = total;
        this.orderItems = orderItems;
    }

    public static OrderResponse from(final Order order) {
        List<OrderItemResponse> responses = order.getOrderItems()
                .stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getDeliveryFee(), order.getTotal(), responses);
    }

    public Long getOrderId() {
        return orderId;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public int getTotal() {
        return total;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
}
