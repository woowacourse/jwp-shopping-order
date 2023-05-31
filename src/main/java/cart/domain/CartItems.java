package cart.domain;

import java.util.List;
import java.util.Optional;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Optional<CartItem> get(final Long productId, final int quantity) {
        return cartItems.stream()
            .filter(cartItem -> cartItem.getProduct().getId().equals(productId) && cartItem.getQuantity() == quantity)
            .findFirst();
    }
}
