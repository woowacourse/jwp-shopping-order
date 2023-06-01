package cart.domain.cartitem;

import java.util.List;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int calculateTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::calculatePriceOfQuantity)
                .sum();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

}
