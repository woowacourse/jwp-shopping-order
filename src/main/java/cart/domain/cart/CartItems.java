package cart.domain.cart;

import cart.domain.Member;
import java.util.List;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void checkOwner(final Member member) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
    }

    public int getTotalProductPrice() {
        return cartItems.stream()
                .map(CartItem::getTotalProductPrice)
                .reduce(0, Integer::sum);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
