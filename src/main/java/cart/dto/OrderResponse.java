package cart.dto;

import cart.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public final class OrderResponse {
    private final Long orderId;
    private final int totalPrice;
    private final List<CartItemResponse> cartItems;

    public OrderResponse(final Long orderId, final int totalPrice, final List<CartItemResponse> cartItems) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.cartItems = cartItems;
    }

    public static OrderResponse from(final Order order) {
        final List<CartItemResponse> items = order.getCartItems().stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getTotalPrice(), items);
    }

    public Long getOrderId() {
        return orderId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }
}
