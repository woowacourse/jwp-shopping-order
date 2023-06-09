package cart.dto.response;

import static java.util.stream.Collectors.toList;

import cart.domain.Order;
import java.util.List;

public class OrderResponse {

    private Long orderId;
    private Integer deliveryFee;
    private Integer total;
    private List<OrderItemResponse> orderItems;

    public OrderResponse() {
    }

    public OrderResponse(Long orderId, List<OrderItemResponse> orderItems, Integer deliveryFee, Integer total) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.deliveryFee = deliveryFee;
        this.total = total;
    }

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .collect(toList());

        return new OrderResponse(order.getId(), orderItems, 3000, order.getTotalPrice());
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public Integer getTotal() {
        return total;
    }
}
