package cart.domain;

import java.util.List;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public static CartItems from(final List<CartItem> cartItems) {
        return new CartItems(cartItems);
    }

    public boolean isNotSameSize(final int size) {
        return cartItems.size() != size;
    }

    public Member getMember() {
        return cartItems.get(0).getMember();
    }

    public Long getMemberId() {
        return cartItems.get(0).getMemberId();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
