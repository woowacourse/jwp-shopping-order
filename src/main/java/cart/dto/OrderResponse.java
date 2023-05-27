package cart.dto;

import java.util.List;

public class OrderResponse {
    private final Long id;
    private final Integer totalPrice;
    private final List<CartItemResponse> cartItems;

    public OrderResponse(Long id, Integer totalPrice, List<CartItemResponse> cartItems) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.cartItems = cartItems;
    }

    public Long getId() {
        return id;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }
}
