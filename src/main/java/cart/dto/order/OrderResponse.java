package cart.dto.order;

import cart.domain.order.Order;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    @JsonProperty("orderId")
    private Long id;
    private int deliveryFee;
    private int total;
    private List<OrderItemResponse> orderItems;

    private OrderResponse() {
    }

    public OrderResponse(final Long id, final int deliveryFee, final int total, final List<OrderItemResponse> orderItems) {
        this.id = id;
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

    public Long getId() {
        return id;
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
