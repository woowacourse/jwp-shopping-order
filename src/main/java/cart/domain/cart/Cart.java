package cart.domain.cart;

import cart.domain.product.Price;
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

    public Price getTotalPrice() {
        return cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(Price.minPrice(), Price::add);
    }
}
