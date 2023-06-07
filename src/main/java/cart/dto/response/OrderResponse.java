package cart.dto.response;

import cart.domain.OrderEntity;

import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final String orderDateTime;
    private final List<OrderItemResponse> orderItems;
    private final int totalPrice;

    public OrderResponse(Long orderId, String orderDateTime, List<OrderItemResponse> orderItems, int totalPrice) {
        this.orderId = orderId;
        this.orderDateTime = orderDateTime;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
    }

    public static OrderResponse of(final OrderEntity orderEntity, final List<OrderItemResponse> orderItemResponses) {
        return new OrderResponse(
                orderEntity.getId(),
                orderEntity.getCreatedAt(),
                orderItemResponses,
                orderEntity.getTotalPrice()
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
