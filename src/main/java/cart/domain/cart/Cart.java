package cart.domain.cart;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<CartItem> cartItems;

    public Cart(final List<CartItem> cartItems) {
        this.cartItems = new ArrayList<>(cartItems);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
