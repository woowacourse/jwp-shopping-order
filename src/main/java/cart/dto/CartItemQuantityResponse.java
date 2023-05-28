package cart.dto;

import cart.domain.cartitem.CartItem;

public class CartItemQuantityResponse {

    private final Long id;
    private final int quantity;

    private CartItemQuantityResponse(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static CartItemQuantityResponse from(CartItem cartItem) {
        return new CartItemQuantityResponse(cartItem.getId(), cartItem.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }
}
