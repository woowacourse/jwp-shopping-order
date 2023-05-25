package cart.domain;

import java.util.List;

public class CartItems {
    private final List<CartItem> cartItems;

    public CartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer calculatePriceSum() {
        return cartItems.stream()
                .mapToInt(CartItem::calculateTotalPrice)
                .sum();
    }
}
