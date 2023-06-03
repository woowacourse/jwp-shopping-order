package cart.domain.cart;

import static cart.exception.badrequest.BadRequestErrorType.CART_ITEM_EMPTY;

import cart.domain.Member;
import cart.exception.badrequest.BadRequestException;
import java.util.List;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void checkNotEmpty() {
        if (cartItems.isEmpty()) {
            throw new BadRequestException(CART_ITEM_EMPTY);
        }
    }

    public boolean hasSize(final int size) {
        return cartItems.size() == size;
    }

    public void checkOwner(final Member member) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
    }

    public int calculateTotalProductPrice() {
        return cartItems.stream()
                .map(CartItem::calculateTotalProductPrice)
                .reduce(0, Integer::sum);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
