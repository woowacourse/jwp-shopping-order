package cart.domain;

import java.util.List;

public class Cart {
    private final List<CartItem> cartItems;

    public Cart(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

}
