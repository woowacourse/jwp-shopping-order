package cart.domain.cart;

import java.util.List;

public class Cart {

    private final List<Cart> cartItems;

    public Cart(final List<Cart> cartItems) {
        this.cartItems = cartItems;
    }

    public List<Cart> getCartItems() {
        return cartItems;
    }


}
