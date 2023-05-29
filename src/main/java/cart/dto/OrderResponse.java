package cart.dto;

import java.util.List;

public final class OrderResponse {
    private final Long orderId;
    private final int totalPrice;
    private final List<CartItemResponse> cartItems;

    public OrderResponse(final Long orderId, final int totalPrice, final List<CartItemResponse> cartItems) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.cartItems = cartItems;
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
