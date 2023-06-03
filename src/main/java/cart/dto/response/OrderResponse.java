package cart.dto.response;

import static java.util.stream.Collectors.toList;

import cart.domain.Order;
import java.util.List;

public class OrderResponse {

    private Long id;
    private List<OrderItemResponse> orderItems;
    private Integer deliveryFee;
    private Integer total;

    public OrderResponse() {
    }

    public OrderResponse(Long id, List<OrderItemResponse> orderItems, Integer deliveryFee, Integer total) {
        this.id = id;
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

    public Long getId() {
        return id;
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
