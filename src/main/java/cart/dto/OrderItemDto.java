package cart.dto;

import cart.domain.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemDto {
    private final Long OrderId;
    private final List<CartItemResponse> orderItems;

    private OrderItemDto(Long orderId, List<CartItem> orderItems) {
        this.OrderId = orderId;
        this.orderItems = orderItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public static OrderItemDto of(Long orderId, List<CartItem> cartItems) {
        return new OrderItemDto(orderId, cartItems);
    }

    public Long getOrderId() {
        return OrderId;
    }

    public List<CartItemResponse> getOrderItems() {
        return orderItems;
    }

}
