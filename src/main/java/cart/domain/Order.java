package cart.domain;

import java.util.List;

public class Order {

    private List<CartItem> cartItems;

    public Order(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
