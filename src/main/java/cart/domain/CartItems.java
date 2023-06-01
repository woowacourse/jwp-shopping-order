package cart.domain;

import java.util.List;

import cart.exception.PriceNotMatchException;

public class CartItems {

    private final List<CartItem> cartItems;

    private CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public static CartItems of(final List<CartItem> cartItems) {
        return new CartItems(cartItems);
    }

    public void validateTotalPrice(final Price expectedTotalPrice) {
        final Price realTotalPrice = getTotalPrice();
        if (expectedTotalPrice.equals(realTotalPrice)) {
            return;
        }
        throw new PriceNotMatchException(expectedTotalPrice.getValue(), realTotalPrice.getValue());
    }

    public Price getTotalPrice() {
        return Price.valueOf(cartItems.stream()
                .mapToInt(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum());
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
