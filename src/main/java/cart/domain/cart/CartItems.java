package cart.domain.cart;

import cart.domain.Member;
import cart.exception.StoreException;
import java.util.List;

public class CartItems {

    private final List<CartItem> cartItems;

    public CartItems(final List<CartItem> cartItems) {
        validate(cartItems);
        this.cartItems = cartItems;
    }

    private void validate(final List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            throw new StoreException("장바구니 상품이 없습니다.");
        }
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
