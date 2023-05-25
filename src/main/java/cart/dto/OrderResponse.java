package cart.dto;

import java.util.List;

public final class OrderResponse {
    private final Long id;
    private final int discountedPrice;
    private final List<CartItemResponse> cartItems;

    public OrderResponse(final Long id, final int discountedPrice, final List<CartItemResponse> cartItems) {
        this.id = id;
        this.discountedPrice = discountedPrice;
        this.cartItems = cartItems;
    }

    public Long getId() {
        return id;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }
}
